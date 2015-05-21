package com.ondrejruttkay.weather;


public class WeatherConfig {
    public static final boolean LOGS = BuildConfig.LOGS;
    public static final boolean DEV_API = BuildConfig.DEV_API;

    public static final String API_ENDPOINT_PRODUCTION = "http://example.com/api/";
    public static final String API_ENDPOINT_DEVELOPMENT = "http://dev.example.com/api/";

    public static String SHOW_PLAY_SERVICES_ERROR_ACTION = "com.ondrejruttkay.weather.SHOW_PLAY_SERVICES_ERROR_ACTION";

    // Result request codes
    public final static int PLAY_SERVICES_FAILURE_RESOLUTION_REQUEST = 1001;
}
