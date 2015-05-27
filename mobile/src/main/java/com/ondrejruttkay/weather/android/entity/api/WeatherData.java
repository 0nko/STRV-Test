package com.ondrejruttkay.weather.android.entity.api;

/**
 * Created by Onko on 5/24/2015.
 */
public class WeatherData {
    private String description;
    private String icon;

    public String getDescription() {
        return Character.toUpperCase(description.charAt(0)) + description.substring(1);
    }


    public String getIcon() {
        return icon;
    }
}
