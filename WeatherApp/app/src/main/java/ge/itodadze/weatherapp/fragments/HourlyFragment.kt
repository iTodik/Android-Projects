package ge.itodadze.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ge.itodadze.weatherapp.MainActivity
import ge.itodadze.weatherapp.R
import ge.itodadze.weatherapp.adapters.HourlyListAdapter
import ge.itodadze.weatherapp.models.ForecastResponse
import ge.itodadze.weatherapp.models.RowItem
import ge.itodadze.weatherapp.models.hourlyToRowItem

class HourlyFragment : Fragment() {

    private lateinit var adapter: HourlyListAdapter
    private lateinit var fragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.hourly, container, false)
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
        val recycler: RecyclerView = fragmentView.findViewById(R.id.recycler_hourly)
        adapter = HourlyListAdapter(emptyList())
        recycler.adapter = adapter
        return fragmentView
    }

    fun update(response: ForecastResponse, currCity: String) {
        fragmentView.findViewById<TextView>(R.id.city).text = currCity
        val forecast: ArrayList<RowItem> = ArrayList()
        for (hourly in response.list) {
            forecast.add(hourlyToRowItem(hourly))
        }
        adapter.update(forecast)
    }

}
