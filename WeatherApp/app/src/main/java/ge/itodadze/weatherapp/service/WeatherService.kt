package ge.itodadze.weatherapp.service

import ge.itodadze.weatherapp.models.ForecastResponse
import ge.itodadze.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey: String, @Query("units") units: String): Call<WeatherResponse>

    @GET("forecast")
    fun getForecast(@Query("q") cityName: String, @Query("appid") apiKey: String, @Query("units") units: String): Call<ForecastResponse>

}