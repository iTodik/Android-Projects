package ge.itodadze.weatherapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.itodadze.weatherapp.fragments.DailyFragment
import ge.itodadze.weatherapp.fragments.HourlyFragment
import ge.itodadze.weatherapp.models.ForecastResponse
import ge.itodadze.weatherapp.models.WeatherResponse

class ViewPagerAdapter(private val activity: FragmentActivity, private val fragments: List<Fragment>):
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun updateDaily(daily: WeatherResponse, currCity: String){
        (fragments[0] as DailyFragment).update(daily, currCity)
    }

    fun updateHourly(hourly: ForecastResponse, currCity: String){
        (fragments[1] as HourlyFragment).update(hourly, currCity)
    }

}