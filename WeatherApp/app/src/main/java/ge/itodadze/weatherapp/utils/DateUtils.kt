package ge.itodadze.weatherapp.utils

import java.text.DateFormatSymbols
import java.util.*

class DateUtils {

    companion object {
        fun formatDate(date: String): String {
            val hr: Int = date.substring(11,13).toInt()
            var formatted: String = if (hr > 12) {
                (hr - 12).toString() + " PM "
            } else {
                "$hr AM "
            }
            formatted += date.substring(8, 10) + " "
            formatted += DateFormatSymbols().months[date.substring(5, 7).toInt() - 1].substring(0, 3)
            return formatted
        }

        fun isDay(timezone: Double): Boolean {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis += timezone.toLong() * 1000
            val time = calendar.get(Calendar.HOUR_OF_DAY)
            return time in 7..18
        }
    }

}