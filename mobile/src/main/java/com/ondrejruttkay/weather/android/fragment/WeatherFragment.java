package com.ondrejruttkay.weather.android.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ondrejruttkay.weather.android.R;
import com.ondrejruttkay.weather.android.WeatherApplication;
import com.ondrejruttkay.weather.android.WeatherConfig;
import com.ondrejruttkay.weather.android.client.response.WeatherResponse;
import com.ondrejruttkay.weather.android.event.LocationError;
import com.ondrejruttkay.weather.android.event.LocationFoundEvent;
import com.ondrejruttkay.weather.android.event.SettingsChangedEvent;
import com.ondrejruttkay.weather.android.event.WeatherError;
import com.ondrejruttkay.weather.android.event.WeatherReceivedEvent;
import com.ondrejruttkay.weather.android.utility.Logcat;
import com.ondrejruttkay.weather.android.utility.NetworkManager;
import com.ondrejruttkay.weather.android.utility.Units;
import com.ondrejruttkay.weather.android.view.ViewState;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;


public class WeatherFragment extends Fragment {
    private ViewState mViewState = null;
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefreshing = false;
    private WeatherResponse mWeatherData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main_weather, container, false);

        setupSwipeRefresh();
        WeatherApplication.getEventBus().register(this);

        return mRootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // load and show data
        if (mViewState == null || mViewState == ViewState.OFFLINE) {
            loadData();
        } else if (mViewState == ViewState.CONTENT) {
            if (mWeatherData != null)
                renderView();
            showContent();
        } else if (mViewState == ViewState.PROGRESS) {
            showProgress();
        } else if (mViewState == ViewState.EMPTY) {
            showEmpty();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.fragment_title_today);
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save current instance state
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        WeatherApplication.getEventBus().unregister(this);
    }


    @Subscribe
    public void onLocationFound(LocationFoundEvent event) {
        Logcat.d("Weather location received");

        // if Geolocation service is obtaining fresh location, wait for it
        if (!WeatherApplication.getGeolocation().isGettingLocation()) {
            Location location = event.getLocation();
            WeatherApplication.getWeatherApiClient().sendWeatherRequest(location.getLatitude(), location.getLongitude());
        }
    }


    @Subscribe
    public void onWeatherReceived(WeatherReceivedEvent event) {
        Logcat.d("Weather data received");

        mWeatherData = event.getWeather();
        renderView();
        showContent();

        mIsRefreshing = false;
    }


    @Subscribe
    public void onWeatherError(WeatherError error) {
        showError(error.getMessage());
    }


    @Subscribe
    public void onLocationError(LocationError error) {
        showError(error.getMessage());
    }


    @Subscribe
    public void onSettingsChanged(SettingsChangedEvent event) {
        if (mViewState == ViewState.CONTENT)
            renderView();
    }


    private void setupSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.global_color_primary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                loadData();
            }
        });
    }


    private void showError(String message) {
        showEmpty();
        mIsRefreshing = false;

        if (!message.isEmpty())
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


    private void loadData() {
        if (NetworkManager.isOnline(getActivity())) {
            // show progress
            if (!mSwipeRefreshLayout.isRefreshing())
                showProgress();

            WeatherApplication.getGeolocation().requestLocation();
        } else {
            showOffline();
        }
    }


    private void showContent() {
        // show mContentView container
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.VISIBLE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);

        mViewState = ViewState.CONTENT;
    }


    private void showProgress() {
        // show progress container
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.VISIBLE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);

        mViewState = ViewState.PROGRESS;
    }


    private void showOffline() {
        // show offline container
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.VISIBLE);
        containerEmpty.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);

        mViewState = ViewState.OFFLINE;
    }


    private void showEmpty() {
        // show empty container
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);

        mViewState = ViewState.EMPTY;
    }


    private void renderView() {
        TextView location = (TextView) mRootView.findViewById(R.id.weather_location);
        TextView weatherSummary = (TextView) mRootView.findViewById(R.id.weather_summary);
        TextView humidity = (TextView) mRootView.findViewById(R.id.weather_humidity);
        TextView precipitation = (TextView) mRootView.findViewById(R.id.weather_precipitation);
        TextView pressure = (TextView) mRootView.findViewById(R.id.weather_pressure);
        TextView windSpeed = (TextView) mRootView.findViewById(R.id.weather_wind_speed);
        TextView windDirection = (TextView) mRootView.findViewById(R.id.weather_wind_direction);
        ImageView weatherImage = (ImageView) mRootView.findViewById(R.id.weather_image);

        Picasso.with(getActivity()).load(WeatherConfig.API_IMAGE_URL + mWeatherData.getWeatherData().getIcon() + ".png").into(weatherImage);

        location.setText(mWeatherData.getCityName());
        weatherSummary.setText(Units.getTemperature(mWeatherData.getInfo().getTemperature())
                + " | " + mWeatherData.getWeatherData().getDescription());
        humidity.setText(mWeatherData.getInfo().getHumidity() + Units.HUMIDITY_UNITS);
        precipitation.setText(Units.getShortDistance(mWeatherData.getRain().getPrecipitation()));
        pressure.setText(String.format("%.2f %s", mWeatherData.getInfo().getPressure(), Units.PRESSURE_UNITS));
        windSpeed.setText(Units.getSpeed(mWeatherData.getWind().getSpeed()));
        windDirection.setText(mWeatherData.getWind().getDirection().name());
    }
}
