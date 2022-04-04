package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.WeatherObject

class Communicator: ViewModel() {
    val Weather = MutableLiveData<WeatherObject?>()

    fun setWeather(value: WeatherObject?) {
        Weather.value = value
    }
}