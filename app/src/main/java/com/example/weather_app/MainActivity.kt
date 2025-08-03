package com.example.weather_app

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager

class MainActivity : AppCompatActivity() {
    private lateinit var weatherTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var cityEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTextView = findViewById(R.id.txt3)
        cityTextView = findViewById(R.id.txt2)
        cityEditText = findViewById(R.id.edittxt)

        cityEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val city = cityEditText.text.toString().trim()
                if (city.isNotEmpty()) {
                    fetchWeatherData(city)
                    // Hide keyboard after search
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(cityEditText.windowToken, 0)
                }
                true // Return true to consume the action
            } else {
                false
            }
        }
    }

    private fun fetchWeatherData(city: String) {
        weatherTextView.text = "Loading..." // Show loading message
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(city = city)

                withContext(Dispatchers.Main) {
                    cityTextView.text = city
                    val weatherInfo = """
                        Temperature: ${response.main.temp}°C
                        Condition: ${response.weather[0].main}
                        Humidity: ${response.main.humidity}%
                        Wind: ${response.wind.speed} m/s
                    """.trimIndent()

                    weatherTextView.text = weatherInfo
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    weatherTextView.text = "Error: ${e.message}"
                    Log.e("WeatherApp", "API Error", e)
                }
            }
        }
    }
}