package com.readnews.app2018;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readnews.app2018.common.APICommon;
import com.readnews.app2018.common.APIService;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sanglx on 1/10/18.
 */

public class MyApplication extends Application {

    private static MyApplication _instance = null;

    public static MyApplication getInstance() {
        return _instance;
    }

    private APIService _service;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private static final String MY_PREFS_NAME = "my_prefs";

    private void initAPIService() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APICommon.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        _service = retrofit.create(APIService.class);
    }



    public APIService getService() {
        return _service;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initAPIService();
        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
