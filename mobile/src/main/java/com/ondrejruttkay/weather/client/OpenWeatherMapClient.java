package com.ondrejruttkay.weather.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ondrejruttkay.weather.WeatherApplication;
import com.ondrejruttkay.weather.WeatherConfig;
import com.ondrejruttkay.weather.client.request.WeatherApiRequest;
import com.ondrejruttkay.weather.client.response.WeatherResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Onko on 5/24/2015.
 */
public class OpenWeatherMapClient {

    private static final String units = "metric";
    private static final int forecastDays = 5;
    private WeatherApiRequest weatherApi;

    public OpenWeatherMapClient() {
        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WeatherConfig.API_ENDPOINT_DEVELOPMENT)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        weatherApi = restAdapter.create(WeatherApiRequest.class);

        WeatherApplication.getEventBus().register(this);
    }

    Callback<WeatherResponse> weatherCallback = new Callback<WeatherResponse>() {
        @Override
        public void success(WeatherResponse elevationResponse, Response response) {
        }

        @Override
        public void failure(RetrofitError retrofitError) {
//            OnkoCycleApp.Bus.post(new ElevationError(retrofitError.getMessage(), retrofitError.isNetworkError()));
        }
    };

    Callback<WeatherResponse> forecastCallback = new Callback<WeatherResponse>() {
        @Override
        public void success(WeatherResponse elevationResponse, Response response) {
        }

        @Override
        public void failure(RetrofitError retrofitError) {
//            OnkoCycleApp.Bus.post(new ElevationError(retrofitError.getMessage(), retrofitError.isNetworkError()));
        }
    };

    public void requestCurrentWeather(String latitude, String longitude) {
        weatherApi.getCurrentWeather(latitude, longitude, units, WeatherConfig.API_KEY, weatherCallback);
    }

    public void requestForecast(String latitude, String longitude) {
        weatherApi.getForecast(latitude, longitude, units, forecastDays, WeatherConfig.API_KEY, forecastCallback);
    }
}
