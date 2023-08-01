package ge.itodadze.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.itodadze.weatherapp.R
import ge.itodadze.weatherapp.models.RowItem

class HourlyListAdapter(private var hourly: List<RowItem>): RecyclerView.Adapter<RowItemViewHolder>() {

    override fun getItemCount(): Int {
        return hourly.size
    }

    override fun onBindViewHolder(holder: RowItemViewHolder, position: Int) {
        holder.date.text = hourly[position].date
        holder.temperature.text = hourly[position].temperature
        holder.description.text = hourly[position].description
        val icon: String = hourly[position].icon
        val imageUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
        Glide.with(holder.itemView).load(imageUrl).into(holder.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowItemViewHolder {
        return RowItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false))
    }

    fun update(newHourly: List<RowItem>) {
        hourly = newHourly
        notifyDataSetChanged()
    }

}

class RowItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var date: TextView = itemView.findViewById(R.id.date)
    var icon: ImageView = itemView.findViewById(R.id.icon)
    var temperature: TextView = itemView.findViewById(R.id.temperature)
    var description: TextView = itemView.findViewById(R.id.description)
}