package com.ondrejruttkay.weather;


public class WeatherConfig {
    public static final boolean LOGS = BuildConfig.LOGS;
    public static final boolean DEV_API = BuildConfig.DEV_API;

    public static final String API_ENDPOINT_PRODUCTION = "http://api.openweathermap.org/";
    public static final String API_ENDPOINT_DEVELOPMENT = "http://api.openweathermap.org/";
    public static final String API_IMAGE_URL = "http://openweathermap.org/img/w/";
    public static final String API_KEY = "63d03d4b0e37a033b9e66906efbb9334";

    public static String SHOW_PLAY_SERVICES_ERROR_ACTION = "com.ondrejruttkay.weather.SHOW_PLAY_SERVICES_ERROR_ACTION";

    // Result request codes
    public final static int PLAY_SERVICES_FAILURE_RESOLUTION_REQUEST = 1001;
}
