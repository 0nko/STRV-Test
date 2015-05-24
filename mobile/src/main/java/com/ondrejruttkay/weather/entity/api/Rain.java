package com.ondrejruttkay.weather.entity.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onko on 5/24/2015.
 */
public class Rain {
    @SerializedName(value = "3h")
    private int precipitation;


    public int getPrecipitation() {
        return precipitation;
    }
}
