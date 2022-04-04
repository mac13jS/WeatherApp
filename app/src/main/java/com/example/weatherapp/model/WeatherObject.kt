package com.example.weatherapp.model

data class WeatherObject(
    var name: String,
    var currentTime: String,
    var temperature: Int,
    var pressure: Int,
    var description: String,
    var sunrise: String,
    var sunset: String,
    var time: String,
    var icon: String
)
