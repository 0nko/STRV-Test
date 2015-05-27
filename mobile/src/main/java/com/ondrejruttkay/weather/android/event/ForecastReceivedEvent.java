package com.ondrejruttkay.weather.android.event;

import com.ondrejruttkay.weather.android.client.response.ForecastResponse;

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
