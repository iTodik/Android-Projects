package ge.itodadze.alarmapp.utils

class TimeFormatter {

    companion object {
        fun timeToColonSeparatedTime(hour: Int, minute: Int): String {
            var hourStr: String = hour.toString()
            var minStr: String = minute.toString()
            if (hour < 10) {
                hourStr = "0$hourStr"
            }
            if (minute < 10) {
                minStr = "0$minStr"
            }
            return "$hourStr:$minStr"
        }
    }

}