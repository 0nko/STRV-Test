package com.ondrejruttkay.weather.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.Toast;

import com.ondrejruttkay.weather.R;
import com.ondrejruttkay.weather.WeatherApplication;
import com.ondrejruttkay.weather.adapter.ForecastListAdapter;
import com.ondrejruttkay.weather.entity.api.ForecastDetails;
import com.ondrejruttkay.weather.event.ForecastError;
import com.ondrejruttkay.weather.event.ForecastReceivedEvent;
import com.ondrejruttkay.weather.event.LocationError;
import com.ondrejruttkay.weather.event.LocationFoundEvent;
import com.ondrejruttkay.weather.event.SettingsChangedEvent;
import com.ondrejruttkay.weather.utility.Logcat;
import com.ondrejruttkay.weather.utility.NetworkManager;
import com.ondrejruttkay.weather.view.ViewState;
import com.squareup.otto.Subscribe;


public class ForecastFragment extends Fragment {
    private ViewState mViewState = null;
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ForecastDetails[] mForecastData;


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
        mRootView = inflater.inflate(R.layout.fragment_main_forecast, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.global_color_primary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        WeatherApplication.getEventBus().register(this);

        return mRootView;
    }


    @Subscribe
    public void onLocationFound(LocationFoundEvent event) {
        Logcat.d("Forecast location received");

        // if Geolocation service is obtaining fresh location, wait for it
        if (!WeatherApplication.getGeolocation().isGettingLocation()) {
            Location location = event.getLocation();
            WeatherApplication.getWeatherApiClient().requestForecast(location.getLatitude(), location.getLongitude());
        }
    }


    @Subscribe
    public void onForecastReceived(ForecastReceivedEvent event) {
        Logcat.d("Forecast data received");

        mForecastData = event.getForecast().getForecastData();

        renderView();
        showContent();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onWeatherError(ForecastError error) {
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

        // load and show data
        if (mViewState == null || mViewState == ViewState.OFFLINE) {
            loadData();
        } else if (mViewState == ViewState.CONTENT) {
            if (mForecastData != null)
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

        getActivity().setTitle(R.string.title_forecast);
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        WeatherApplication.getEventBus().unregister(this);
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

        mSwipeRefreshLayout.setEnabled(true);
        mViewState = ViewState.EMPTY;
    }


    private void renderView() {
        ListView forecastList = (ListView) mRootView.findViewById(R.id.forecast_list);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        animation.setDuration(250);
        LayoutAnimationController controller = new LayoutAnimationController(animation);

        forecastList.setLayoutAnimation(controller);
        forecastList.setAdapter(new ForecastListAdapter(getActivity(), mForecastData));
    }
}
