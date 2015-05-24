package com.ondrejruttkay.weather.client.request;

import com.ondrejruttkay.weather.client.response.WeatherResponse;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Onko on 5/24/2015.
 */
public interface WeatherApiRequest {
    @POST("/data/2.5/weather")
    void getCurrentWeather(@Query("lat") String latitude,
                           @Query("lon") String longitude,
                           @Query("units") String units,
                           @Query("APPID") String apiKey,
                           Callback<WeatherResponse> callback);

    @POST("/data/2.5/forecast")
    void getForecast(@Query("lat") String latitude,
                     @Query("lon") String longitude,
                     @Query("units") String units,
                     @Query("cnt") int days,
                     @Query("APPID") String apiKey,
                     Callback<WeatherResponse> callback);
}
