package com.gamecard.rule2018;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements  BillingProcessor.IBillingHandler{


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LICENSE_KEY = "";
    private static final String PRODUCT_ID = "android.test.purchased";

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

    private static long back_pressed;
    private Class<? extends Fragment> currentFragmentClass = null;

    @Override
    public void onBackPressed() {
        if(currentFragmentClass != GameListFragment.class) {
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
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.INTERNET)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
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
        addFragment(GameListFragment.getNewInstance(), false);
    }

    public void purchaseIAP() {
        if (!readyToPurchase) {
            Toast.makeText(getApplicationContext(), "Not ready to purchased", Toast.LENGTH_SHORT).show();
            return;
        }
        bp.purchase(this, PRODUCT_ID);
    }

    private GameItem gameItem = null;

    public void onEvent(GameClickEvent evt) {
        Log.d(TAG, "on Event");
        gameItem = evt.gameItem;
        if(1 != 0) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MainActivity.this);
            }
            builder.setTitle("Thông báo")
                    .setMessage("Bạn có muốn xem luật game?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            if (!readyToPurchase) {
                                Toast.makeText(getApplicationContext(), "Not ready to purchased", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            bp.purchase(MainActivity.this, PRODUCT_ID);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

        } else {
            addFragment(DetailFragment.getNewInstance(gameItem.getGameName(), gameItem.getUrl()), true);
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
            addFragment(DetailFragment.getNewInstance(gameItem.getGameName(), gameItem.getUrl()), true);
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
