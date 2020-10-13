package com.zhy.proxy_retrofit.api;

import com.zhy.proxy_retrofit.myrefrofit.annotation.Field;
import com.zhy.proxy_retrofit.myrefrofit.annotation.GET;
import com.zhy.proxy_retrofit.myrefrofit.annotation.POST;
import com.zhy.proxy_retrofit.myrefrofit.annotation.Query;

import okhttp3.Call;

public interface MyWeatherApi {
    @POST("/v3/weather/weatherInfo")
    Call postWeather(@Field("city") String city, @Field("key") String key);


    @GET("/v3/weather/weatherInfo")
    Call getWeather(@Query("city") String city, @Query("key") String key);
}
