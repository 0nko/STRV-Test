package com.ondrejruttkay.weather;

import android.app.Application;
import android.content.Context;

import com.ondrejruttkay.weather.client.OpenWeatherMapClient;
import com.ondrejruttkay.weather.geolocation.Geolocation;
import com.ondrejruttkay.weather.utility.Preferences;
import com.squareup.otto.Bus;


public class WeatherApplication extends Application {
    private static WeatherApplication mInstance;
    private static Bus mEventBus;
    private static Geolocation mGeolocation;
    private static OpenWeatherMapClient mApiClient;
    private static Preferences mPreferences;


    public WeatherApplication() {
        mInstance = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = new Bus();
        mPreferences = new Preferences(this);
        mGeolocation = new Geolocation();
        mApiClient = new OpenWeatherMapClient();

        // force AsyncTask to be initialized in the main thread due to the bug:
        // http://stackoverflow.com/questions/4280330/onpostexecute-not-being-called-in-asynctask-handler-runtime-exception
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Context getContext() {
        return mInstance;
    }


    public static Bus getEventBus() {
        return mEventBus;
    }


    public static Geolocation getGeolocation() {
        return mGeolocation;
    }


    public static OpenWeatherMapClient getWeatherApiClient() {
        return mApiClient;
    }


    public static Preferences getPreferences() {
        return mPreferences;
    }
}
