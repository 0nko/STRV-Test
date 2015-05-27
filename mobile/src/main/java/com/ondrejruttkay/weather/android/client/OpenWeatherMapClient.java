package com.ondrejruttkay.weather.android.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ondrejruttkay.weather.android.WeatherApplication;
import com.ondrejruttkay.weather.android.WeatherConfig;
import com.ondrejruttkay.weather.android.client.request.WeatherApiRequest;
import com.ondrejruttkay.weather.android.client.response.ForecastResponse;
import com.ondrejruttkay.weather.android.client.response.WeatherResponse;
import com.ondrejruttkay.weather.android.event.ForecastError;
import com.ondrejruttkay.weather.android.event.ForecastReceivedEvent;
import com.ondrejruttkay.weather.android.event.WeatherError;
import com.ondrejruttkay.weather.android.event.WeatherReceivedEvent;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Onko on 5/24/2015.
 */
public class OpenWeatherMapClient {

    private static final String UNITS = "metric";
    private static final int FORECAST_DAYS = 5;

    Callback<WeatherResponse> weatherCallback = new Callback<WeatherResponse>() {
        @Override
        public void success(WeatherResponse weatherResponse, Response response) {
            WeatherApplication.getEventBus().post(new WeatherReceivedEvent(weatherResponse));
        }


        @Override
        public void failure(RetrofitError retrofitError) {
            WeatherApplication.getEventBus().post(new WeatherError(retrofitError.getMessage(), retrofitError.isNetworkError()));
        }
    };

    Callback<ForecastResponse> forecastCallback = new Callback<ForecastResponse>() {
        @Override
        public void success(ForecastResponse forecastResponse, Response response) {
            WeatherApplication.getEventBus().post(new ForecastReceivedEvent(forecastResponse));
        }


        @Override
        public void failure(RetrofitError retrofitError) {
            WeatherApplication.getEventBus().post(new ForecastError(retrofitError.getMessage(), retrofitError.isNetworkError()));
        }
    };

    private WeatherApiRequest weatherApi;

    public OpenWeatherMapClient() {
        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WeatherConfig.API_ENDPOINT_DEVELOPMENT)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        weatherApi = restAdapter.create(WeatherApiRequest.class);
    }


    public void sendWeatherRequest(double latitude, double longitude) {
        weatherApi.sendWeatherRequest(latitude, longitude, UNITS, WeatherConfig.API_KEY, weatherCallback);
    }


    public void sendForecastRequest(double latitude, double longitude) {
        weatherApi.sendForecastRequest(latitude, longitude, UNITS, FORECAST_DAYS, WeatherConfig.API_KEY, forecastCallback);
    }
}
