package com.ondrejruttkay.weather.event;

/**
 * Created by Onko on 5/24/2015.
 */
public class ForecastError extends ErrorEventBase {

    public ForecastError(String message, boolean isNetworkError) {
        super(message, isNetworkError);
    }
}
