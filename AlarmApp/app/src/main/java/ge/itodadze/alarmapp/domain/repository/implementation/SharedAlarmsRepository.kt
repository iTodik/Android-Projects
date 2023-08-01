package ge.itodadze.alarmapp.domain.repository.implementation

import android.content.SharedPreferences
import ge.itodadze.alarmapp.domain.repository.AlarmsRepository
import ge.itodadze.alarmapp.viewmodel.model.AlarmItem

class SharedAlarmsRepository(private val sharedPreferences: SharedPreferences): AlarmsRepository {

    companion object {
        private const val ALARMS: String = "alarms"
    }

    override fun add(alarm: AlarmItem) {
        var alarms: MutableSet<String>? = sharedPreferences.getStringSet(ALARMS, null)?.toHashSet()
        if (alarms == null) alarms = HashSet()
        alarms.add(alarm.serialize())
        sharedPreferences.edit().putStringSet(ALARMS, alarms).apply()
    }

    override fun remove(alarm: AlarmItem) {
        var alarms: MutableSet<String>? = sharedPreferences.getStringSet(ALARMS, null)?.toHashSet()
        if (alarms == null) alarms = HashSet()
        alarms.remove(alarm.serialize())
        sharedPreferences.edit().putStringSet(ALARMS, alarms).apply()
    }

    override fun getAll(): List<AlarmItem?> {
        val alarms: MutableSet<String> = sharedPreferences.getStringSet(ALARMS, null) ?: return emptyList()
        return alarms.toList().map{AlarmItem.deserialize(it)}
    }

    override fun update(alarm: AlarmItem, newStatus: Boolean) {
        var alarms: MutableSet<String>? = sharedPreferences.getStringSet(ALARMS, null)?.toHashSet()
        if (alarms == null) alarms = HashSet()
        alarms.remove(alarm.serialize())
        alarms.add(AlarmItem(alarm.time, newStatus).serialize())
        sharedPreferences.edit().putStringSet(ALARMS, alarms).apply()
    }

}