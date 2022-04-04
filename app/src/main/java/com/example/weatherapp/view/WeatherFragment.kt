package com.example.weatherapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherObject
import com.example.weatherapp.viewmodel.Communicator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

class WeatherFragment : Fragment() {
    private var communicator: Communicator ?= null
    private var weather: WeatherObject ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() = runBlocking {
        val scope = CoroutineScope(Dispatchers.Main + Job())

        scope.launch { getWeather() }
        scope.launch { getData() }
    }

    private fun getWeather() {
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]
        communicator!!.Weather.observe(viewLifecycleOwner) { o -> weather = o }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        view?.findViewById<TextView>(R.id.city)?.text = weather!!.name
        view?.findViewById<TextView>(R.id.currentTime)?.text = weather!!.currentTime
        view?.findViewById<TextView>(R.id.temp)?.text = weather!!.temperature.toString() + " \u2103"
        view?.findViewById<TextView>(R.id.pressure)?.text = weather!!.pressure.toString() + " hPa"
        view?.findViewById<TextView>(R.id.desc)?.text = weather!!.description
        view?.findViewById<TextView>(R.id.sunrise)?.text = weather!!.sunrise
        view?.findViewById<TextView>(R.id.sunset)?.text = weather!!.sunset
        view?.findViewById<TextView>(R.id.time)?.text = weather!!.time

        val imageUrl = "http://openweathermap.org/img/wn/" + weather!!.icon + "@2x.png"
        val imageView: ImageView = view?.findViewById(R.id.icon)!!
        Picasso.get().load(imageUrl).resize(250, 250).into(imageView)
    }
}