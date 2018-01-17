package com.readnews.app2018.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.readnews.app2018.event.InstallReferrerEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by sanglx on 1/10/18.
 */

public class InstallReferrerReceiver extends BroadcastReceiver {
    private static final String TAG = InstallReferrerReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Log.d(TAG, "referrer:" + referrer);
        EventBus.getDefault().post(new InstallReferrerEvent(referrer));
    }
}
