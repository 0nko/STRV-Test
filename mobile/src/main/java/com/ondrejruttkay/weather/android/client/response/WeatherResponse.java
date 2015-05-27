package com.ondrejruttkay.weather.android.client.response;

import com.ondrejruttkay.weather.android.entity.api.Clouds;
import com.ondrejruttkay.weather.android.entity.api.Rain;
import com.ondrejruttkay.weather.android.entity.api.WeatherData;
import com.ondrejruttkay.weather.android.entity.api.WeatherDetails;
import com.ondrejruttkay.weather.android.entity.api.Wind;

/*
{"coord":{"lon":139,"lat":35},
"sys":{"country":"JP","sunrise":1369769524,"sunset":1369821049},
"weather":[{"id":804,"main":"clouds","description":"overcast clouds","icon":"04n"}],
"main":{"temp":289.5,"humidity":89,"pressure":1013,"temp_min":287.04,"temp_max":292.04},
"wind":{"speed":7.31,"deg":187.002},
"rain":{"3h":0},
"clouds":{"all":92},
"dt":1369824698,
"id":1851632,
"name":"Shuzenji",
"cod":200}
 */
public class WeatherResponse {
    private String name;
    private WeatherData[] weather;
    private WeatherDetails main;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;


    public WeatherResponse() {
        // initialize objects in case some data is missing
        main = new WeatherDetails();
        wind = new Wind();
        rain = new Rain();
        clouds = new Clouds();
    }


    public String getCityName() {
        return name;
    }


    public WeatherData getWeatherData() {
        if (weather != null && weather.length == 1)
            return weather[0];
        return new WeatherData();
    }


    public WeatherDetails getInfo() {
        return main;
    }


    public Wind getWind() {
        return wind;
    }


    public Rain getRain() {
        return rain;
    }


    public Clouds getClouds() {
        return clouds;
    }


}
