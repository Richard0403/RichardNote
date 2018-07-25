package me.richard.note.net.api;

import java.util.Map;

import me.richard.note.net.entity.WeatherEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by XiaoU on 2018/7/25.
 */

public interface OtherService {

    @GET("v3/weather/now.json?")
    Call<WeatherEntity> getWeather(@QueryMap Map<String,Object> params);

}
