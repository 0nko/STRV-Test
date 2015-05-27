package com.ondrejruttkay.weather.android.utility;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ondrejruttkay.weather.android.WeatherApplication;
import com.ondrejruttkay.weather.android.WeatherConfig;

/**
 * Created by Onko on 7/9/13.
 */
public class PlayServices {

    private static final String PLAY_SERVICES_ERROR_DIALOG_TAG = "play_services_error_dialog";
    private static boolean mResolvingError;
    private static ConnectionResult mResult;
    private static GoogleApiClient mApiClient;


    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    public static boolean isConnected(AppCompatActivity activity) {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            GooglePlayServicesUtil.showErrorDialogFragment(resultCode, activity, WeatherConfig.PLAY_SERVICES_FAILURE_RESOLUTION_REQUEST);
            return false;
        }
    }


    public static void tryResolveError(ConnectionResult connectionResult, GoogleApiClient client, Class<?> activityType) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        }

        mResult = connectionResult;
        mApiClient = client;
        startActivityForResolution(activityType);
    }


    private static void startActivityForResolution(Class<?> activityType) {
        Intent intent = new Intent(WeatherApplication.getContext(), activityType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(WeatherConfig.SHOW_PLAY_SERVICES_ERROR_ACTION);
        WeatherApplication.getContext().startActivity(intent);
    }


    public static void onConnectionFailed(AppCompatActivity activity) {
        if (mResult.hasResolution()) {
            try {
                mResolvingError = true;
                mResult.startResolutionForResult(activity, WeatherConfig.PLAY_SERVICES_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showPlayServicesErrorDialog(activity);
        }
    }


    public static void retryConnect() {
        mApiClient.connect();
    }


    public static void showPlayServicesErrorDialog(AppCompatActivity activity) {
        if (!activity.isFinishing()) {
            mResolvingError = true;
            FragmentManager fm = activity.getSupportFragmentManager();
            PlayServicesErrorDialog f = (PlayServicesErrorDialog) fm.findFragmentByTag(PLAY_SERVICES_ERROR_DIALOG_TAG);
            if (f == null) {
                PlayServicesErrorDialog errorDialog = new PlayServicesErrorDialog();
                errorDialog.show(fm, PLAY_SERVICES_ERROR_DIALOG_TAG);
            } else {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(f);
                ft.add(f, PLAY_SERVICES_ERROR_DIALOG_TAG);
                ft.commit();
            }
        }
    }


    public static boolean isResolvingError() {
        return mResolvingError;
    }

    public static class PlayServicesErrorDialog extends DialogFragment {

        public PlayServicesErrorDialog() {
            setRetainInstance(true);
        }


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setCancelable(false);
            return GooglePlayServicesUtil.getErrorDialog(mResult.getErrorCode(), getActivity(), WeatherConfig.PLAY_SERVICES_FAILURE_RESOLUTION_REQUEST);
        }


        @Override
        public void onDismiss(DialogInterface dialog) {
            mResolvingError = false;
        }
    }
}
