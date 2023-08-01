package ge.itodadze.alarmapp.domain.repository

import ge.itodadze.alarmapp.viewmodel.model.AlarmItem

interface AlarmsRepository {
    fun add(alarm: AlarmItem)
    fun remove(alarm: AlarmItem)
    fun getAll(): List<AlarmItem?>
    fun update(alarm: AlarmItem, newStatus: Boolean)
}