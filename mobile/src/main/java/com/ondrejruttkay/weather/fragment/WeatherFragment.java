package com.ondrejruttkay.weather.fragment;

import android.app.Activity;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.WeatherApplication;
import com.ondrejruttkay.weather.client.response.WeatherResponse;
import com.ondrejruttkay.weather.event.LocationFoundEvent;
import com.ondrejruttkay.weather.event.WeatherReceivedEvent;
import com.ondrejruttkay.weather.utility.NetworkManager;
import com.ondrejruttkay.weather.view.ViewState;
import com.squareup.otto.Subscribe;

import java.io.IOException;


public class WeatherFragment extends Fragment {
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

        mSwipeRefreshLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.global_color_primary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        if (mWeatherData != null) {
            renderView();
            showContent();
        }

        return mRootView;
    }


    @Subscribe
    public void onLocationFound(LocationFoundEvent event) {
        Location location = event.getLocation();
        WeatherApplication.getWeatherApiClient().requestCurrentWeather(location.getLatitude(), location.getLongitude());
    }

    @Subscribe
    public void onWeatherReceived(WeatherReceivedEvent event) {
        mWeatherData = event.getWeather();
        renderView();
        showContent();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
    public void onStart() {
        super.onStart();

        WeatherApplication.getEventBus().register(this);
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
        TextView location = (TextView)mRootView.findViewById(R.id.weather_location);
        TextView weatherSummary = (TextView)mRootView.findViewById(R.id.weather_summary);
        TextView humidity = (TextView)mRootView.findViewById(R.id.weather_humidity);
        TextView precipitation = (TextView)mRootView.findViewById(R.id.weather_precipitation);
        TextView pressure = (TextView)mRootView.findViewById(R.id.weather_pressure);
        TextView windSpeed = (TextView)mRootView.findViewById(R.id.weather_wind_speed);
        TextView windDirection = (TextView)mRootView.findViewById(R.id.weather_wind_direction);

        location.setText(mWeatherData.getCityName());
        weatherSummary.setText(mWeatherData.getWeatherData().getDescription());
        humidity.setText(mWeatherData.getInfo().getHumidity() + "%");
        precipitation.setText(mWeatherData.getRain().getPrecipitation() + " mm");
        pressure.setText(mWeatherData.getInfo().getPressure() + " hPa");
        windSpeed.setText(mWeatherData.getWind().getSpeed() + " km/h");
        windDirection.setText(mWeatherData.getWind().getDirection().name());
    }
}
