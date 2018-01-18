package com.readnews.app2018.common;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sanglx on 1/10/18.
 */

public interface APIService {
    @GET("api.php/sangSms")
    Call<BaseModel> getPurchaseAPI(@Query("transId") String transId,
                                   @Query("productId") String productId,
                                   @Query("key") String key,
                                   @Query("phone") String phone,
                                   @Query("deviceId") String deviceId);
}