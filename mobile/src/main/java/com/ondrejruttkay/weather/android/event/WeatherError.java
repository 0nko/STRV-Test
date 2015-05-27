package com.ondrejruttkay.weather.android.event;

/**
 * Created by Onko on 5/24/2015.
 */
public class WeatherError extends ErrorEventBase {

    public WeatherError(String message, boolean isNetworkError) {
        super(message, isNetworkError);
    }
}
