package com.example.weatherapp.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherObject
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.viewmodel.Communicator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class MainFragmentEasy : Fragment() {
    private var communicator: Communicator?= null
    private lateinit var city: EditText
    private val AppId = "03c7a6deadafd061f8745b69d85adffb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {}
        callback.isEnabled = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.main_fragment_easy, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        menu.findItem(R.id.simplify).title = "Tryb zwykły"

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val builder = AlertDialog.Builder(context)
            .setMessage("Czy chcesz wyłączyć tryb uproszczony aplikacji?")
            .setCancelable(false)
            .setNegativeButton("NIE") { dialog, _ -> dialog.cancel() }
            .setPositiveButton("TAK") { _, _ ->
                findNavController().navigate(R.id.action_mainFragmentEasy_to_mainFragment)
            }

        val alert = builder.create()
        alert.show()

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        city = view.findViewById(R.id.editTextSearchEasy)

        city.setOnClickListener {
            city.setText("")
        }

        view.findViewById<Button>(R.id.buttonSearchEasy).apply {
            setOnClickListener{
                keyboardHide()
                getWeather()
            }
        }
    }

    private fun keyboardHide() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != requireActivity().currentFocus) imm.hideSoftInputFromWindow(
            requireActivity().currentFocus!!
                .applicationWindowToken, 0
        )
    }

    private fun getWeather() = runBlocking {
        val scope = CoroutineScope(Dispatchers.Main + Job())

        scope.launch {
            val response = WeatherRepository.getWeatherData(city.text.toString(), AppId)

            if (response !== null) {
                communicator!!.setWeather(
                    WeatherObject(
                        response.name.toString(),
                        unixConvert(System.currentTimeMillis() / 1000, "date"),
                        response.main!!.temp.minus(273.15).roundToInt(),
                        response.main!!.pressure.roundToInt(),
                        response.weather[0].description!!,
                        unixConvert(response.sys!!.sunrise.toLong(), "time"),
                        unixConvert(response.sys!!.sunset.toLong(), "time"),
                        unixConvert(response.dt.toLong(), "date"),
                        response.weather[0].icon!!
                    )
                )

                findNavController().navigate(R.id.action_mainFragmentEasy_to_weatherFragmentEasy)
                city.setText("")
            }
            else {
                view?.let {
                    Snackbar.make(
                        it,
                        "Nie znaleziono miasta: ${city.text}",
                        Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun unixConvert(timestamp: Long, mode: String): String {
        return if (mode == "time") {
            val sdf = java.text.SimpleDateFormat("HH:mm")
            val date = java.util.Date(timestamp * 1000)

            sdf.format(date)
        } else {
            val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm")
            val date = java.util.Date(timestamp * 1000)

            sdf.format(date)
        }
    }
}