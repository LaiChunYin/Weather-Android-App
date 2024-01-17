package com.example.weather_ken.api

import com.example.weather_ken.models.WeatherAPIResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MyInterface {
    // @GET , @Path = an annotation
    @GET("timeline/{latitude},{longitude}/today")
    suspend fun getWeather(@Path("latitude") latitude: Double, @Path("longitude") longitude: Double,
                           @Query("key") apiKey: String,
                           @Query("unitGroup") unitGroup: String = "metric",
                           @Query("elements") elements: String = "datetime,temp,humidity,conditions",
                           @Query("include") include: String = "current",
                           @Query("contentType") contentType: String = "json"): WeatherAPIResult
}