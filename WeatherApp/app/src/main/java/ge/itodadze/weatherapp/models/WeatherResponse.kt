package ge.itodadze.weatherapp.models

data class WeatherResponse(val timezone: Double, val main: MainInfo, val weather: List<ExtraInfo>)