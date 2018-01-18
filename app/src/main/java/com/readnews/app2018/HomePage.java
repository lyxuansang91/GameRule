package com.readnews.app2018;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.readnews.app2018.common.APICommon;
import com.readnews.app2018.common.APIService;
import com.readnews.app2018.common.BaseModel;
import com.readnews.app2018.event.InstallReferrerEvent;
import com.readnews.app2018.event.LoginEvent;
import com.readnews.com.readnews.app2018.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BillingProcessor.IBillingHandler {

    private static final String TAG = HomePage.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl37cs65mLZIK8afS6knAcmPXOnUfACqFHnpE2C9/A8uwob7xfLBqwljRtg8xsO115F3Q8ku1ULSMAnOLcDYZ+Yd86iZGjkOwzeJhHhVACtfe8n5ZkIua2HgMc9Kcsri/RALXyh3cLGvnLjnaOYRzv/GaWvxw3ExkFp042lWvjZPEl2h82kjShMm6oX53YOtezd6CspIQuEDS/jY4p2kKNadMAn0uIZVi3HnFgpS3ITeg9UUZOT+c6wPD0W0NaWsd7ZKj4YGBN4etfVOyNCjMnquRL5HaK+00m40m6op169aSGYYnS6RM0nz1P8unXl/Svv74DrtnmUshpAciAR4ecQIDAQAB";
    TextView tvPhone;

    private ArrayList<String> productIdList = new ArrayList<String>();

    private BillingProcessor bp;

    private FragmentManager fragmentManager;
    private boolean readyToPurchase = false;
    String productId;
    private String PRODUCT_ID = "android.test.purchased";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission granted");
                } else {
                    Toast.makeText(HomePage.this, "permission denied!", Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private Item item = null;

    public String referrer = null;

    public void onEvent(InstallReferrerEvent event) {
        this.referrer = event.referrer;
        Log.d(TAG, "referrence code: " + getReferenceCode());
    }

    public static final boolean TEST = false;

    public String getReferenceCode() {

        Log.d(TAG, "referrence Code:" + referrer);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "iap");
        Log.d(TAG, "folder:" + folder.toString());
        boolean success;
        if(!folder.exists()) {
            success = folder.mkdirs();
            Log.d(TAG, "success:" + success);
            if(!success) {
                return referrer;
            }
        }

        try {
            File file = new File(folder, "referrer");
            StringBuilder builder = new StringBuilder();
            if(file.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;

                if((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
            }

            Log.d(TAG, "reader builder:" + builder.toString());

            boolean isFoundReferrer = (referrer != null);

            if(isFoundReferrer) {

            } else {
                String _refer = builder.toString();
                if(!_refer.isEmpty()) {
                    referrer = _refer;
                } else {
                    referrer = getPackageName();
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            sb.append(referrer);
            fos.write(sb.toString().getBytes());
            fos.close();
            return referrer;

        } catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public String getPackageName() {
        String packageName = "";
        try {
            packageName = getApplicationContext().getPackageName();
            Log.d(TAG, packageName);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            packageName = "";
        }
        return packageName;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestPurchase(String productId) {
        if(TEST)
            productId = PRODUCT_ID;
        APIService service = MyApplication.getInstance().getService();
        String transId = APICommon.getTransId();
        String key = APICommon.getKey(transId);
        String phone = MyApplication.getInstance().sharedPreferences.getString("phone", "");
        Call<BaseModel> call =  service.getPurchaseAPI(transId, productId, phone, APICommon.getDeviceId(HomePage.this), key);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                Log.d(TAG, "get purchase api response:" + response.body());
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                Log.d(TAG, "error:" + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void requestPermission() {
        // Log.d(TAG, "referrer:" + getReferenceCode());
        if (ContextCompat.checkSelfPermission(HomePage.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(HomePage.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(HomePage.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)  {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(HomePage.this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_INTERNET);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEvent(ClickEvent evt) {
        Log.d(TAG, "on Event");
        String phone = MyApplication.getInstance().sharedPreferences.getString("phone", "");
        if(phone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }
        item = evt.item;
        if(1 != 0) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(HomePage.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(HomePage.this);
            }
            builder.setTitle("Thông báo")
                    .setMessage("Bạn có muốn xem trang tin tức này?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            if (!readyToPurchase) {
                                Toast.makeText(getApplicationContext(), "Not ready to purchased", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(TEST) HomePage.this.productId = PRODUCT_ID;
                            bp.purchase(HomePage.this, HomePage.this.productId);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).show();

        } else {
            addFragment(DetailFragment.getNewInstance(item.getName(), item.getUrl()), true);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestPermission();
        initUI();

        productId = productIdList.get(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        tvPhone = (TextView) header.findViewById(R.id.tvPhone);
        tvPhone.setText(MyApplication.getInstance().sharedPreferences.getString("phone", "Vui lòng nhập số điện thoại"));
        navigationView.setCheckedItem(0);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "on click login");
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private static long back_pressed;
    private Class<? extends Fragment> currentFragmentClass = null;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, " current fragment class:" + currentFragmentClass.getSimpleName());
            if(currentFragmentClass != NewsListFragment.class) {
                super.onBackPressed();
                return;
            }
            if (System.currentTimeMillis() - back_pressed < 2000) {
                finish();
            } else {
                Toast.makeText(HomePage.this, getResources().getString(R.string.ClickBackAgainToExitTheApplication), Toast.LENGTH_SHORT).show();
            }

            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_20k) {
            // Handle the camera action
            this.productId = productIdList.get(0);
        } else if (id == R.id.nav_50k) {
            this.productId = productIdList.get(1);
        } else if (id == R.id.nav_100k) {
            this.productId = productIdList.get(2);
        } else if(id == R.id.nav_200k) {
            this.productId = productIdList.get(3);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        if(addToBackStack)
            ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public void onEvent(LoginEvent event) {
        String phone = event.phone;
        Log.d(TAG, "on Login event");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        tvPhone = (TextView) header.findViewById(R.id.tvPhone);
        tvPhone.setText(MyApplication.getInstance().sharedPreferences.getString("phone", "Vui lòng nhập số điện thoại"));
        getSupportActionBar().setTitle(MyApplication.getInstance().sharedPreferences.getString("phone", "+84123456789"));
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.d(TAG, "on product purchased:" + productId + ", transaction detail: " + details.purchaseInfo.responseData);
        boolean consumed = bp.consumePurchase(productId);
        if (consumed) {
            requestPurchase(productId);
            Log.d(TAG, "item:" + item);
            addFragment(DetailFragment.getNewInstance(item.getName(), item.getUrl()), true);
        }
    }

    private void initUI() {
        View view = findViewById(R.id.content);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fr = fragmentManager.findFragmentById(R.id.content);
                currentFragmentClass = fr.getClass();
            }
        });
        bp = new BillingProcessor(this, LICENSE_KEY, this);
        bp.initialize();
        addFragment(NewsListFragment.getNewInstance(), false);
        getSupportActionBar().setTitle(MyApplication.getInstance().sharedPreferences.getString("phone", "+84123456789"));
        productIdList.add("com.readnews.app2018.20");
        productIdList.add("com.readnews.app2018.50");
        productIdList.add("com.readnews.app2018.100");
        productIdList.add("com.readnews.app2018.200");
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.d(TAG, "On billing error:" + errorCode);
    }

    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
    }


}
