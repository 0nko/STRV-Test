package com.ondrejruttkay.weather.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.WeatherApplication;
import com.ondrejruttkay.weather.client.request.WeatherApiRequest;
import com.ondrejruttkay.weather.client.response.WeatherResponse;
import com.ondrejruttkay.weather.event.LocationError;
import com.ondrejruttkay.weather.event.LocationFoundEvent;
import com.ondrejruttkay.weather.event.SettingsChangedEvent;
import com.ondrejruttkay.weather.event.WeatherError;
import com.ondrejruttkay.weather.event.WeatherReceivedEvent;
import com.ondrejruttkay.weather.utility.Logcat;
import com.ondrejruttkay.weather.utility.NetworkManager;
import com.ondrejruttkay.weather.utility.Units;
import com.ondrejruttkay.weather.view.ViewState;
import com.squareup.otto.Subscribe;


public class WeatherFragment extends Fragment {

    private static final String PRESSURE_UNITS = " hPa";
    private static final String HUMIDITY_UNITS = "%";

    private ViewState mViewState = null;
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private WeatherResponse mWeatherData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main_weather, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.global_color_primary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        return mRootView;
    }


    @Subscribe
    public void onLocationFound(LocationFoundEvent event) {
        Logcat.d("Weather location received");

        Location location = event.getLocation();
        WeatherApplication.getWeatherApiClient().requestCurrentWeather(location.getLatitude(), location.getLongitude());
    }


    @Subscribe
    public void onWeatherReceived(WeatherReceivedEvent event) {
        Logcat.d("Weather data received");

        mWeatherData = event.getWeather();
        renderView();
        showContent();
        mSwipeRefreshLayout.setRefreshing(false);
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


    private void showError(String message) {
        showEmpty();
        mSwipeRefreshLayout.setRefreshing(false);

        if (!message.isEmpty())
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

        WeatherApplication.getEventBus().register(this);

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

        getActivity().setTitle(R.string.title_today);
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();

        WeatherApplication.getEventBus().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save current instance state
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }


    private void loadData() {
        if (NetworkManager.isOnline(getActivity())) {
            // show progress
            if (!mSwipeRefreshLayout.isRefreshing())
                showProgress();

            WeatherApplication.getGeolocation().requestFreshLocation();
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

        location.setText(mWeatherData.getCityName());
        weatherSummary.setText(Units.getTemperature(mWeatherData.getInfo().getTemperature())
                + " | " +  mWeatherData.getWeatherData().getDescription());
        humidity.setText(mWeatherData.getInfo().getHumidity() + HUMIDITY_UNITS);
        precipitation.setText(Units.getShortDistance(mWeatherData.getRain().getPrecipitation()));
        pressure.setText(mWeatherData.getInfo().getPressure() + PRESSURE_UNITS);
        windSpeed.setText(Units.getSpeed(mWeatherData.getWind().getSpeed()));
        windDirection.setText(mWeatherData.getWind().getDirection().name());
    }
}
