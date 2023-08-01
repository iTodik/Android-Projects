package ge.itodadze.weatherapp.models

import ge.itodadze.weatherapp.utils.DateUtils

data class HourlyResponse(val dt_txt: String, val main: MainInfo, val weather: List<ExtraInfo>)

fun hourlyToRowItem(hourly: HourlyResponse): RowItem {
    val date: String = DateUtils.formatDate(hourly.dt_txt)
    val icon = hourly.weather[0].icon
    val temperature = hourly.main.temp.toInt().toString() + "°"
    val description = hourly.weather[0].description
    return RowItem(date, icon, temperature, description)
}