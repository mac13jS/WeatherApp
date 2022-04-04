package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

class WeatherResponse {
    @SerializedName("weather")
    var weather = ArrayList<Weather>()

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("dt")
    var dt = 0

    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String? = null
}

class Weather {
    @SerializedName("id")
    var id = 0

    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null
}

class Main {
    @SerializedName("temp")
    var temp = 0f

    @SerializedName("pressure")
    var pressure = 0f
}

class Sys {
    @SerializedName("id")
    var id = 0

    @SerializedName("sunrise")
    var sunrise = 0

    @SerializedName("sunset")
    var sunset = 0
}

