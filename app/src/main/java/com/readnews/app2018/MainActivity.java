package com.readnews.app2018;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.readnews.app2018.common.APICommon;
import com.readnews.app2018.common.APIService;
import com.readnews.app2018.common.BaseModel;
import com.readnews.app2018.event.InstallReferrerEvent;
import com.readnews.com.readnews.app2018.R;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  BillingProcessor.IBillingHandler{


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl37cs65mLZIK8afS6knAcmPXOnUfACqFHnpE2C9/A8uwob7xfLBqwljRtg8xsO115F3Q8ku1ULSMAnOLcDYZ+Yd86iZGjkOwzeJhHhVACtfe8n5ZkIua2HgMc9Kcsri/RALXyh3cLGvnLjnaOYRzv/GaWvxw3ExkFp042lWvjZPEl2h82kjShMm6oX53YOtezd6CspIQuEDS/jY4p2kKNadMAn0uIZVi3HnFgpS3ITeg9UUZOT+c6wPD0W0NaWsd7ZKj4YGBN4etfVOyNCjMnquRL5HaK+00m40m6op169aSGYYnS6RM0nz1P8unXl/Svv74DrtnmUshpAciAR4ecQIDAQAB";
    private static final String PRODUCT_ID = "com.readnews.app2018";

    public static int MY_PERMISSIONS_REQUEST_INTERNET = 1;

    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initUI();
    }



    private void requestPurchase(String productId) {
        APIService service = MyApplication.getInstance().getService();
        String transId = APICommon.getTransId();
        String key = APICommon.getKey(transId);
        Call<BaseModel> call =  service.getPurchaseAPI(transId, productId, APICommon.getDeviceId(MainActivity.this), key);
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


    private static long back_pressed;
    private Class<? extends Fragment> currentFragmentClass = null;

    @Override
    public void onBackPressed() {
        if(currentFragmentClass != NewsListFragment.class) {
            super.onBackPressed();
            return;
        }
        if (System.currentTimeMillis() - back_pressed < 2000) {
            finish();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.ClickBackAgainToExitTheApplication), Toast.LENGTH_SHORT).show();
        }

        back_pressed = System.currentTimeMillis();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission granted");
                } else {
                    Toast.makeText(MainActivity.this, "permission denied!", Toast.LENGTH_SHORT).show();
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestPermission() {
        Log.d(TAG, "referrer:" + getReferenceCode());
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)  {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_INTERNET);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }
    }

    private void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        if(addToBackStack)
            ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private FragmentManager fragmentManager;

    private void initUI() {
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
    }

    public void purchaseIAP() {
        if (!readyToPurchase) {
            Toast.makeText(getApplicationContext(), "Not ready to purchased", Toast.LENGTH_SHORT).show();
            return;
        }
        bp.purchase(this, PRODUCT_ID);
    }

    private Item item = null;

    public String referrer = null;

    public void onEvent(InstallReferrerEvent event) {
        this.referrer = event.referrer;
        Log.d(TAG, "referrence code: " + getReferenceCode());
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

    public static final boolean TEST = false;

    public String getReferenceCode() {
        if(TEST) {
            referrer = "1234567";
        }
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


    public void onEvent(ClickEvent evt) {
        Log.d(TAG, "on Event");
        item = evt.item;
        if(1 != 0) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MainActivity.this);
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
                            bp.purchase(MainActivity.this, getReferenceCode());
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).setItems(new CharSequence[]{"1", "2", "3"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(TAG, "on click" + i);
                        }
            }).show();

        } else {
            addFragment(DetailFragment.getNewInstance(item.getName(), item.getUrl()), true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.d(TAG, "on product purchased:" + productId + ", transaction detail: " + details.purchaseInfo.responseData);
        boolean consumed = bp.consumePurchase(PRODUCT_ID);
        if (consumed) {
            requestPurchase(productId);
            Log.d(TAG, "item:" + item);
            addFragment(DetailFragment.getNewInstance(item.getName(), item.getUrl()), true);
        }
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
