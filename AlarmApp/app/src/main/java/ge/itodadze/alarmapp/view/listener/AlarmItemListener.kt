package ge.itodadze.alarmapp.view.listener

import ge.itodadze.alarmapp.viewmodel.model.AlarmItem

interface AlarmItemListener {
    fun alarmChecked(alarm: AlarmItem, status: Boolean)
    fun alarmLongClicked(alarm: AlarmItem)
}