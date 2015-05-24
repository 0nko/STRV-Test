package com.ondrejruttkay.weather.entity.api;

import java.util.Date;

/**
 * Created by Onko on 5/24/2015.
 */
public class ForecastDetails {
    private long dt;
    private WeatherData[] weather;
    private Temperature temp;


    public Date getDate() {
        return new Date(dt * 1000);
    }


    public WeatherData getWeatherData() {
        if (weather != null)
            return weather[0];
        return null;
    }


    public Temperature getTemperature() {
        return temp;
    }
}
