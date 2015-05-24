package com.ondrejruttkay.weather.client.response;

import com.ondrejruttkay.weather.entity.api.City;
import com.ondrejruttkay.weather.entity.api.ForecastDetails;

/**
 * Created by Onko on 5/24/2015.
 */

/*
{"cod":"200","message":0.0637,"city":{"id":524901,"name":"Moscow","coord":{"lon":37.615555,"lat":55.75222},"country":"RU","population":1000000},"cnt":2,
"list":[
{"dt":1432458000,"temp":{"day":285.54,"min":281.51,"max":285.54,"night":281.51,"eve":285.54,"morn":285.54},"pressure":1008.78,"humidity":97,
"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":4.62,"deg":338,"clouds":92,"rain":2.45},
{"dt":1432544400,"temp":{"day":290.42,"min":282.22,"max":291.74,"night":283.86,"eve":291.68,"morn":282.22},"pressure":1013.45,"humidity":71,
"weather":[{"id":800,"main":"Clear","description":"sky is clear","icon":"01d"}],"speed":1.71,"deg":72,"clouds":0}
]}
 */
public class ForecastResponse {
    private City city;
    private ForecastDetails[] list;

    public City getCity() {
        return city;
    }


    public ForecastDetails[] getForecast() {
        return list;
    }


}
