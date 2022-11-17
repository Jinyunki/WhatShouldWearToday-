package com.example.weathertest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("getVilageFcst?numOfRows=10&pageNo=1&dataType=JSON")
    Call<WeatherResponse> getCurrentWeather(@Query("serviceKey") String serviceKey,
                                            @Query("base_date") String base_date,
                                            @Query("base_time") String base_time,
                                            @Query("nx") int nx,
                                            @Query("ny") int ny);
}
