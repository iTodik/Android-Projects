package ge.itodadze.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ge.itodadze.weatherapp.MainActivity
import ge.itodadze.weatherapp.R
import ge.itodadze.weatherapp.models.WeatherResponse

class DailyFragment: Fragment() {

    private lateinit var fragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.today, container, false)
        val uk: ImageView = fragmentView.findViewById(R.id.uk)
        val georgia: ImageView = fragmentView.findViewById(R.id.georgia)
        val jamaica: ImageView = fragmentView.findViewById(R.id.jamaica)
        uk.setOnClickListener {
            fragmentView.findViewById<TextView>(R.id.city).text =
                (activity as MainActivity).updateCity(R.id.uk)
        }
        georgia.setOnClickListener {
            fragmentView.findViewById<TextView>(R.id.city).text =
                (activity as MainActivity).updateCity(R.id.georgia)
        }
        jamaica.setOnClickListener {
            fragmentView.findViewById<TextView>(R.id.city).text =
                (activity as MainActivity).updateCity(R.id.jamaica)
        }
        return fragmentView
    }

    fun update(response: WeatherResponse, currCity: String) {
        fragmentView.findViewById<TextView>(R.id.city).text = currCity
        val tempText: String = response.main.temp.toInt().toString() + "°"
        fragmentView.findViewById<TextView>(R.id.temperature).text = tempText
        fragmentView.findViewById<TextView>(R.id.temperature_content).text = tempText
        fragmentView.findViewById<TextView>(R.id.description).text = response.weather[0].description.uppercase()
        val feelsLikeText: String = response.main.feels_like.toInt().toString() + "°"
        fragmentView.findViewById<TextView>(R.id.feels_like_content).text = feelsLikeText
        val humidityText: String = response.main.humidity.toString() + "%"
        fragmentView.findViewById<TextView>(R.id.humidity_content).text = humidityText
        fragmentView.findViewById<TextView>(R.id.pressure_content).text = response.main.pressure.toString()
        val icon: String = response.weather[0].icon
        val url = "https://openweathermap.org/img/wn/$icon@2x.png"
        Glide.with(this).load(url).into(fragmentView.findViewById(R.id.icon))
    }
}
