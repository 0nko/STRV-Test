package com.ondrejruttkay.weather.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ondrejruttkay.weather.android.R;
import com.ondrejruttkay.weather.android.WeatherApplication;
import com.ondrejruttkay.weather.android.WeatherConfig;
import com.ondrejruttkay.weather.android.event.SettingsChangedEvent;
import com.ondrejruttkay.weather.android.fragment.AlertDialogFragment;
import com.ondrejruttkay.weather.android.fragment.ForecastFragment;
import com.ondrejruttkay.weather.android.fragment.WeatherFragment;
import com.ondrejruttkay.weather.android.utility.Logcat;
import com.ondrejruttkay.weather.android.utility.PlayServices;
import com.ondrejruttkay.weather.android.view.FragmentNavigationDrawer;


public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {

    public static int TOOLBAR_ELEVATION = 15;

    private boolean mPreferencesChanged = false;
    private boolean mShowPlayServicesError = false;

    private FragmentNavigationDrawer mDrawerLayout;
    private Toolbar mToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (FragmentNavigationDrawer) findViewById(R.id.navigation_drawer);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(TOOLBAR_ELEVATION);

        setupDrawer(savedInstanceState);

        // register listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerLayout.syncState();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mShowPlayServicesError) {
            mShowPlayServicesError = false;
            PlayServices.showPlayServicesErrorDialog(this);
        }
    }


    @Override
    public void onDestroy() {
        // unregister listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // remove background to reduce overdraw
        if (hasFocus)
            getWindow().setBackgroundDrawable(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startSettingsActivity();
                break;
            case R.id.menu_about:
                showAboutDialog();
                break;
        }
        return true;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mPreferencesChanged = true;
    }


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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getAction().equals(WeatherConfig.SHOW_PLAY_SERVICES_ERROR_ACTION)) {
            PlayServices.onConnectionFailed(this);
        }
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


    private void setupDrawer(Bundle savedInstanceState) {
        LinearLayout mDrawerLinearLayout = (LinearLayout) findViewById(R.id.drawerLinearLayout);

        // Setup drawer view
        mDrawerLayout.setupDrawerConfiguration((ListView) findViewById(R.id.left_drawer), mToolbar, mDrawerLinearLayout, R.id.content_frame);

        // Add nav items
        mDrawerLayout.addNavItem(getString(R.string.title_today), R.drawable.ic_drawer_today_dark, getString(R.string.title_today), WeatherFragment.class);
        mDrawerLayout.addNavItem(getString(R.string.title_forecast), R.drawable.ic_drawer_forecast_dark, getString(R.string.title_forecast), ForecastFragment.class);

        // Select default
        if (savedInstanceState == null) {
            mDrawerLayout.selectDrawerItem(0);
        }
    }


    private void refreshData() {
        WeatherApplication.getEventBus().post(new SettingsChangedEvent());
    }


    private void showAboutDialog() {
        AlertDialogFragment aboutDialog = AlertDialogFragment.newInstance(R.string.title_about, R.string.about_message);
        aboutDialog.show(getSupportFragmentManager(), "dialog");
    }


    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
