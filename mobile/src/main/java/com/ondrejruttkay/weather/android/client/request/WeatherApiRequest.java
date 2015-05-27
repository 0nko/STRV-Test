package com.ondrejruttkay.weather.android.client.request;

import com.ondrejruttkay.weather.android.client.response.ForecastResponse;
import com.ondrejruttkay.weather.android.client.response.WeatherResponse;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Onko on 5/24/2015.
 */
public interface WeatherApiRequest {
    @POST("/data/2.5/weather")
    void getCurrentWeather(@Query("lat") double latitude,
                           @Query("lon") double longitude,
                           @Query("units") String units,
                           @Query("APPID") String apiKey,
                           Callback<WeatherResponse> callback);

    @POST("/data/2.5/forecast/daily")
    void getForecast(@Query("lat") double latitude,
                     @Query("lon") double longitude,
                     @Query("units") String units,
                     @Query("cnt") int days,
                     @Query("APPID") String apiKey,
                     Callback<ForecastResponse> callback);
}
