package com.ondrejruttkay.weather.android.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.ondrejruttkay.weather.android.WeatherApplication;
import com.ondrejruttkay.weather.android.entity.LengthUnits;
import com.ondrejruttkay.weather.android.entity.TemperatureUnits;


public class Preferences {
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public Preferences(Context context) {
        if (context == null) context = WeatherApplication.getContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
    }


    public void clearPreferences() {
        Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    // GETTERS ////////////////////////////////////////////////////////////////////////////////////


    public TemperatureUnits getTemperatureUnits() {
        String temperature = mSharedPreferences.getString("pref_temperature_units", "0");
        return TemperatureUnits.values()[Integer.parseInt(temperature)];
    }


    public LengthUnits getLengthUnits() {
        String length = mSharedPreferences.getString("pref_length_units", "0");
        return LengthUnits.values()[Integer.parseInt(length)];
    }


    // SETTERS ////////////////////////////////////////////////////////////////////////////////////


    public void setTemperatureUnits(TemperatureUnits units) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("pref_temperature_units", units.ordinal() + "");
        editor.commit();
    }


    public void setLengthUnits(LengthUnits units) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("pref_length_units", units.ordinal() + "");
        editor.commit();
    }
}
