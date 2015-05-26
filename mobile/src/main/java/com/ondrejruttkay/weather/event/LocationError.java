package com.ondrejruttkay.weather.event;

/**
 * Created by Onko on 5/25/2015.
 */
public class LocationError extends ErrorEventBase {
    public LocationError(String message, boolean isNetworkError) {
        super(message, isNetworkError);
    }
}
