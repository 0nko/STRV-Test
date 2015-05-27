package com.ondrejruttkay.weather.geolocation;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.WeatherApplication;
import com.ondrejruttkay.weather.activity.MainActivity;
import com.ondrejruttkay.weather.event.LocationError;
import com.ondrejruttkay.weather.event.LocationFoundEvent;
import com.ondrejruttkay.weather.utility.Logcat;
import com.ondrejruttkay.weather.utility.PlayServices;
import com.squareup.otto.Produce;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Geolocation implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_UPDATE_TIMEOUT = 30000;
    private static final int ACCEPTABLE_LOCATION_AGE = 300000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Timer mTimer;
    private Location mLastLocation;

    public Geolocation() {
        mLocationRequest = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(WeatherApplication.getContext()).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mTimer = new Timer();

        WeatherApplication.getEventBus().register(this);
    }

    public void requestLocation() {
        // check if we have recent location available
        if (mLastLocation != null) {
            if (mLastLocation.getTime() - new Date().getTime() < ACCEPTABLE_LOCATION_AGE)
                onLocationChanged(mLastLocation);
        }
        else if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting() && !PlayServices.isResolvingError()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Logcat.d("onConnected");

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        scheduleTimeout();
    }


    private void scheduleTimeout() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Logcat.d("timeout reached");
                        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (lastLocation != null)  {
                            onLocationChanged(lastLocation);
                        } else {
                            WeatherApplication.getEventBus().post(new LocationError(WeatherApplication.getContext().getResources().getString(R.string.error_location_not_found), false));
                            stop();
                        }
                    }
                });
            }
        }, LOCATION_UPDATE_TIMEOUT);
        Logcat.d("timeout scheduled");
    }


    public void stop() {
        mTimer.cancel();
        mTimer.purge();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public boolean isGettingLocation() {
        return mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Logcat.d("onConnectionFailed: " + connectionResult.getErrorCode());
        PlayServices.tryResolveError(connectionResult, mGoogleApiClient, MainActivity.class);
        WeatherApplication.getEventBus().post(new LocationError("", true));
    }


    @Override
    public void onLocationChanged(Location location) {
        Logcat.d("Geolocation.onLocationChanged(): " + location.getProvider() + " / " + location.getLatitude() + " / " + location.getLongitude() + " / " + new Date(location.getTime()).toString());

        // return location
        stop();

        WeatherApplication.getEventBus().post(new LocationFoundEvent(location));
        mLastLocation = location;
    }
}
