package com.example.weatherapp.model

import retrofit2.awaitResponse

class WeatherRepository {
    companion object {
        suspend fun getWeatherData(city: String, appId: String): WeatherResponse? {
            return RetrofitInstance.api.getCurrentWeatherData(city, appId, "pl").awaitResponse().body()
        }
    }
}