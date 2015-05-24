package com.ondrejruttkay.weather.entity.event;

import com.ondrejruttkay.weather.client.response.WeatherResponse;

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
