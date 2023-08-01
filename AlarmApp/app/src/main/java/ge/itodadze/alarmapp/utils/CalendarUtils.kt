package ge.itodadze.alarmapp.utils

import java.util.*

class CalendarUtils {

    companion object {
        fun getWithSpecifiedHourMinuteIfFuture(hour: Int, minute: Int): Calendar? {
            val calendar: Calendar = Calendar.getInstance()
            if (hour < calendar.get(Calendar.HOUR_OF_DAY) || (hour == calendar.get(Calendar.HOUR_OF_DAY) &&
                        minute <= calendar.get(Calendar.MINUTE))) {
                return null
            }
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                hour, minute, 0)
            return calendar
        }
    }

}