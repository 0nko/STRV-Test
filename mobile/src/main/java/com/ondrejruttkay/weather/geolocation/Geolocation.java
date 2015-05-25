package com.ondrejruttkay.weather.geolocation;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ondrejruttkay.weather.WeatherApplication;
import com.ondrejruttkay.weather.activity.MainActivity;
import com.ondrejruttkay.weather.event.LocationFoundEvent;
import com.ondrejruttkay.weather.utility.Logcat;
import com.ondrejruttkay.weather.utility.PlayServices;

import java.util.Date;

public class Geolocation implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    public Geolocation() {
        mLocationRequest = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(WeatherApplication.getContext()).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    public void requestFreshLocation() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting() && !PlayServices.isResolvingError()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Logcat.d("onConnected");

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void stop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Logcat.d("onConnectionFailed: " + connectionResult.getErrorCode());
        PlayServices.tryResolveError(connectionResult, mGoogleApiClient, MainActivity.class);
    }


    @Override
    public void onLocationChanged(Location location) {
        Logcat.d("Geolocation.onLocationChanged(): " + location.getProvider() + " / " + location.getLatitude() + " / " + location.getLongitude() + " / " + new Date(location.getTime()).toString());

        // return location
        stop();

        WeatherApplication.getEventBus().post(new LocationFoundEvent(location));
    }
}
