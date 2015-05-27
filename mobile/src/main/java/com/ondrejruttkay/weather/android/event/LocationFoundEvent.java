package com.ondrejruttkay.weather.android.event;

import android.location.Location;

/**
 * Created by Onko on 5/24/2015.
 */
public class LocationFoundEvent {
    private Location location;


    public LocationFoundEvent(Location location) {
        this.location = location;
    }


    public Location getLocation() {
        return location;
    }
}
