package com.ondrejruttkay.weather.event;

import com.ondrejruttkay.weather.client.response.ForecastResponse;

/**
 * Created by Onko on 5/24/2015.
 */
public class ForecastReceivedEvent {

    private ForecastResponse mForecast;

    public ForecastReceivedEvent(ForecastResponse response) {
        mForecast = response;
    }


    public ForecastResponse getForecast() {
        return mForecast;
    }
}
