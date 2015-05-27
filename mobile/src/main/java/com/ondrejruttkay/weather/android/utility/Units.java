package com.ondrejruttkay.weather.android.utility;

import com.ondrejruttkay.weather.android.WeatherApplication;
import com.ondrejruttkay.weather.android.entity.LengthUnits;
import com.ondrejruttkay.weather.android.entity.TemperatureUnits;

/**
 * Created by Onko on 7/8/13.
 */
public class Units {

    public static final String SHORT_DISTANCE_IMPERIAL_UNITS = "in";
    public static final String LONG_DISTANCE_IMPERIAL_UNITS = "mi";
    public static final String SPEED_IMPERIAL_UNITS = "mph";
    public static final String SPEED_METRIC_UNITS = "km/h";
    public static final String SHORT_DISTANCE_METRIC_UNITS = "mm";
    public static final String LONG_DISTANCE_METRIC_UNITS = "km";
    public static final String TEMPERATURE_METRIC_UNITS = "°C";
    public static final String TEMPERATURE_IMPERIAL_UNITS = "°F";

    // multiplication factor to convert kilometers to miles
    public static final double KM_TO_MI = 0.621371192;

    // multiplication factor to convert millimeters to inches
    public static final double MM_TO_INCH = 0.0393701;

    // multiplication factor to convert miles to kilometers
    public static final double MI_TO_KM = 1 / KM_TO_MI;

    // multiplication factor to convert meters to yards
    public static final double M_TO_YD = 1.09361;

    // multiplication factor to convert miles to feet
    public static final double MI_TO_FT = 5280.0;

    // multiplication factor to convert feet to miles
    public static final double FT_TO_MI = 1 / MI_TO_FT;

    // multiplication factor to convert meters to kilometers
    public static final double M_TO_KM = 1 / 1000.0;

    // multiplication factor to convert meters per second to kilometers per hour
    public static final double MS_TO_KMH = 3.6;

    // multiplication factor to convert miliseconds to seconds
    public static final double MS_TO_S = 0.001;

    // multiplication factor to convert meters to miles
    public static final double M_TO_MI = M_TO_KM * KM_TO_MI;

    // multiplication factor to convert meters to feet
    public static final double M_TO_FT = M_TO_MI * MI_TO_FT;

    // multiplication factor to convert degrees to radians
    public static final double DEG_TO_RAD = Math.PI / 180.0;

    // multiplication factor to convert fraction to percentage points
    public static final double FRACTION_TO_PERCENT = 100.0;

    public static double getAdjustedLongDistance(double distance) {
        if (WeatherApplication.getPreferences().getLengthUnits() == LengthUnits.METRIC) {
            return distance;
        } else {
            return distance * KM_TO_MI;
        }
    }

    public static double getAdjustedShortDistance(double distance) {
        if (WeatherApplication.getPreferences().getLengthUnits() == LengthUnits.METRIC) {
            return distance;
        } else {
            return distance * MM_TO_INCH;
        }
    }

    public static String getShortDistance(double distance) {
        if (WeatherApplication.getPreferences().getLengthUnits() == LengthUnits.METRIC) {
            return String.format("%.1f %s", distance, SHORT_DISTANCE_METRIC_UNITS);
        } else {
            return String.format("%.1f %s", getAdjustedShortDistance(distance), SHORT_DISTANCE_IMPERIAL_UNITS);
        }
    }


    public static String getSpeed(double speed) {
        if (WeatherApplication.getPreferences().getLengthUnits() == LengthUnits.METRIC) {
            return String.format("%.1f %s", speed, SPEED_METRIC_UNITS);
        } else {
            return String.format("%.1f %s", getAdjustedLongDistance(speed), SPEED_IMPERIAL_UNITS);
        }
    }

    public static String getTemperature(double temp) {
        if (WeatherApplication.getPreferences().getTemperatureUnits() == TemperatureUnits.METRIC) {
            return String.format("%d%s", Math.round(temp), TEMPERATURE_METRIC_UNITS);
        } else {
            return String.format("%d%s", Math.round(temp * 9.0 / 5.0 + 32), TEMPERATURE_IMPERIAL_UNITS);
        }
    }
}