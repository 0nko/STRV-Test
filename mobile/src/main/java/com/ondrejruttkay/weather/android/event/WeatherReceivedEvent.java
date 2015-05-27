package com.ondrejruttkay.weather.android.event;

import com.ondrejruttkay.weather.android.client.response.WeatherResponse;

/**
 * Created by Onko on 5/24/2015.
 */
public class WeatherReceivedEvent {

    private WeatherResponse mWeather;


    public WeatherReceivedEvent(WeatherResponse response) {
        mWeather = response;
    }


    public WeatherResponse getWeather() {
        return mWeather;
    }
}
