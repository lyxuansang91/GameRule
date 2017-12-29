package com.gamecard.rule2018;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void initUI() {
        addFragment(GameListFragment.getNewInstance());
    }

    public void onEvent(GameClickEvent evt) {
        Log.d(TAG, "on Event");
        GameItem gameItem = evt.gameItem;
        addFragment(DetailFragment.getNewInstance(gameItem.getGameZone()));

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
}
