package com.readnews.app2018.common;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sanglx on 1/10/18.
 */

public class APICommon {
    public static final String BASE_URL = "http://kingviet.top:8080/";
    private static final String TAG = APICommon.class.getSimpleName();

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getKeyTransId() {
        return "kingcard123";
    }

    public static String getKey(String transId) {
        String result = APICommon.getMD5(transId + APICommon.getKeyTransId());
        Log.d(TAG, "get key:" + result);
        return result;
    }

    public static String getTransId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getDeviceId(Context context) {
        String androidDeviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidDeviceId;
    }
}


