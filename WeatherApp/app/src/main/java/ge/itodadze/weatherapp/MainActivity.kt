package ge.itodadze.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import ge.itodadze.weatherapp.adapters.ViewPagerAdapter
import ge.itodadze.weatherapp.fragments.DailyFragment
import ge.itodadze.weatherapp.fragments.HourlyFragment
import ge.itodadze.weatherapp.models.ForecastResponse
import ge.itodadze.weatherapp.models.WeatherResponse
import ge.itodadze.weatherapp.service.WeatherService
import ge.itodadze.weatherapp.utils.DateUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var city: String
    private lateinit var fragments: List<Fragment>
    private lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        city = getString(R.string.tbilisi)
        fragments = listOf(DailyFragment(), HourlyFragment())
        pager = findViewById(R.id.view_pager_2)
        initFragments()
    }

    private fun initFragments() {
        pager.adapter = ViewPagerAdapter(this, fragments)
        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnItemSelectedListener{
            if (it.itemId == R.id.today) {
                pager.currentItem = 0
            } else {
                pager.currentItem = 1
            }
            true
        }
        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    navigation.selectedItemId = R.id.today
                } else {
                    navigation.selectedItemId = R.id.hourly
                }
                weatherUpdate()
            }
        })
    }

    fun updateCity(id: Int): String {
        city = when (id) {
            R.id.georgia -> getString(R.string.tbilisi)
            R.id.uk -> getString(R.string.london)
            R.id.jamaica -> getString(R.string.kingston)
            else -> {"Error: City Not Found"}
        }
        weatherUpdate()
        return city
    }

    private fun weatherUpdate() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.weather_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: WeatherService = retrofit.create(WeatherService::class.java)
        if (pager.currentItem == 0) {
            todayWeather(service)
        } else {
            hourlyWeather(service)
        }
    }

    private fun todayWeather(service: WeatherService) {
        val responseCall: Call<WeatherResponse> = service.getWeather(
            city, getString(R.string.api_key), getString(R.string.units))
        responseCall.enqueue(object: Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    (pager.adapter as ViewPagerAdapter).updateDaily(response.body(), city)
                    if (DateUtils.isDay(response.body().timezone)) {
                        findViewById<View>(R.id.main).setBackgroundColor(getColor(R.color.day_mode))
                    } else {
                        findViewById<View>(R.id.main).setBackgroundColor(getColor(R.color.night_mode))
                    }
                } else {
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                if (t != null) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun hourlyWeather(service: WeatherService) {
        val responseCall: Call<ForecastResponse> = service.getForecast(
            city, getString(R.string.api_key), getString(R.string.units))
        responseCall.enqueue(object: Callback<ForecastResponse>{
            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {
                if (response.isSuccessful) {
                    (pager.adapter as ViewPagerAdapter).updateHourly(response.body(), city)
                } else {
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ForecastResponse>?, t: Throwable?) {
                if (t != null) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

}