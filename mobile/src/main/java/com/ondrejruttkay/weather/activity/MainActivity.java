package com.ondrejruttkay.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.WeatherConfig;
import com.ondrejruttkay.weather.utility.Logcat;
import com.ondrejruttkay.weather.utility.PlayServices;


public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {
    private boolean mPreferencesChanged = false;
    private boolean mShowPlayServicesError = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();

        // preferences have changed so refresh data
        if (mPreferencesChanged) {
            refreshData();
            mPreferencesChanged = false;
        }
    }

    private void refreshData() {
    }


    @Override
    public void onDestroy() {
        // unregister listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefs_key_user_id))) {
            mPreferencesChanged = true;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mShowPlayServicesError) {
            mShowPlayServicesError = false;
            PlayServices.showPlayServicesErrorDialog(this);
        }
    }

    /*
         * Handle results returned to this Activity by other Activities started with
         * startActivityForResult(). In particular, the method onConnectionFailed() in
         * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
         * start an Activity that handles Google Play services problems. The result of this
         * call returns here, to onActivityResult.
         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case WeatherConfig.PLAY_SERVICES_FAILURE_RESOLUTION_REQUEST:

                String message;
                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:
                        PlayServices.retryConnect();
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        message = getString(R.string.dialog_play_services_no_resolution);
                        Logcat.d(message);
                        mShowPlayServicesError = true;
                        break;
                }
                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
//                Logcat.d(getString(R.string.unknown_activity_request_code, requestCode));
                break;
        }
    }
}
