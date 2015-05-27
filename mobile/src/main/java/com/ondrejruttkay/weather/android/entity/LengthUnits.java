package com.ondrejruttkay.weather.android.entity;

/**
 * Created by Onko on 5/26/2015.
 */
public enum LengthUnits {

    METRIC(0),
    IMPERIAL(1);

    private final int id;


    private LengthUnits(int id) {
        this.id = id;
    }
}
