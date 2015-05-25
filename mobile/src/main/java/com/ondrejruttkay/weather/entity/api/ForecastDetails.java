package com.ondrejruttkay.weather.entity.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Onko on 5/24/2015.
 */
public class ForecastDetails {
    private long dt;
    private WeatherData[] weather;
    private Temperature temp;

    public String getDayOfWeek() {
        DateFormat format = new SimpleDateFormat("EEEE");
        String day = format.format(new Date(dt * 1000));
        return day;
    }


    public WeatherData getWeatherData() {
        if (weather != null && weather.length == 1)
            return weather[0];
        return null;
    }


    public Temperature getTemperature() {
        return temp;
    }
}
