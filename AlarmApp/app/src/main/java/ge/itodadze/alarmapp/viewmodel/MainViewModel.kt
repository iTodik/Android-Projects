package ge.itodadze.alarmapp.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import ge.itodadze.alarmapp.R
import ge.itodadze.alarmapp.domain.repository.AlarmsRepository
import ge.itodadze.alarmapp.domain.repository.ModeRepository
import ge.itodadze.alarmapp.domain.repository.implementation.SharedAlarmsRepository
import ge.itodadze.alarmapp.domain.repository.implementation.SharedModeRepository
import ge.itodadze.alarmapp.utils.CalendarUtils
import ge.itodadze.alarmapp.utils.TimeFormatter
import ge.itodadze.alarmapp.viewmodel.model.AlarmItem
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    private val application: Application
): AndroidViewModel(application) {

    private val modeRepository: ModeRepository = SharedModeRepository(
        AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
        application.getSharedPreferences(application.resources.getString(R.string.shared), MODE_PRIVATE)
    )

    private val alarmsRepository: AlarmsRepository = SharedAlarmsRepository(
        application.getSharedPreferences(application.resources.getString(R.string.shared), MODE_PRIVATE)
    )

    private val _mode = MutableLiveData<Boolean>()
    val mode: LiveData<Boolean>get() = _mode

    private var mutableAlarms: List<AlarmItem?> = alarmsRepository.getAll()
    private val _alarms = MutableLiveData<List<AlarmItem?>>()
    val alarms: LiveData<List<AlarmItem?>>get() = _alarms

    init {
        viewModelScope.launch {
            _mode.value = modeRepository.get()
            _alarms.value = mutableAlarms.toList()
        }
    }

    fun switchMode() {
        val mode: Boolean = modeRepository.switch()
        viewModelScope.launch {
            _mode.value = mode
            _alarms.value = mutableAlarms
        }
    }

    fun addAlarm(hour: Int, minute: Int): Boolean {
        val calendar: Calendar = CalendarUtils.getWithSpecifiedHourMinuteIfFuture(hour, minute)
            ?: return false
        val alarm = AlarmItem(TimeFormatter.timeToColonSeparatedTime(hour, minute), true)
        alarmsRepository.add(alarm)
        mutableAlarms = mutableAlarms + alarm
        registerAlarm(calendar, alarm)
        viewModelScope.launch {
            _alarms.value = mutableAlarms.toList()
        }
        return true
    }

    fun changeStatus(alarm: AlarmItem, status: Boolean) {
        val intent: PendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            alarm.hashCode(),
            Intent(AlarmReceiver.ALARM_ACTION).apply {
                `package` = application.packageName
                putExtra(AlarmItem.ITEM_IDENTIFIER, alarm.serialize())
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        if (status) {
            val time: Calendar = CalendarUtils
                .getWithSpecifiedHourMinuteIfFuture(alarm.getHour(), alarm.getMinutes())!!
            (application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager)
                .setExact(AlarmManager.RTC_WAKEUP, time.timeInMillis, intent)
        } else {
            (application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager)
                .cancel(intent)
        }
        mutableAlarms = mutableAlarms.map{
            if (alarm == it) {
                AlarmItem(alarm.time, status)
            } else {
                it
            }
        }
        alarmsRepository.update(alarm, status)
    }

    fun deleteAlarm(alarm: AlarmItem) {
        val intent: PendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            alarm.hashCode(),
            Intent(AlarmReceiver.ALARM_ACTION).apply {
                `package` = application.packageName
                putExtra(AlarmItem.ITEM_IDENTIFIER, alarm.serialize())
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        (application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager)
            .cancel(intent)
        alarmsRepository.remove(alarm)
        val removeIndex: Int = mutableAlarms.indexOf(alarm)
        mutableAlarms = mutableAlarms.subList(0, removeIndex) +
                mutableAlarms.subList(removeIndex + 1, mutableAlarms.size)
        viewModelScope.launch {
            _alarms.value = mutableAlarms
        }
    }

    private fun registerAlarm(time: Calendar, alarm: AlarmItem) {
        val intent: PendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            alarm.hashCode(),
            Intent(AlarmReceiver.ALARM_ACTION).apply {
                `package` = application.packageName
                putExtra(AlarmItem.ITEM_IDENTIFIER, alarm.serialize())
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        (application.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager)
            .setExact(AlarmManager.RTC_WAKEUP, time.timeInMillis, intent)
    }

    companion object {
        fun getMainViewModelFactory(application: Application): MainViewModelFactory {
            return MainViewModelFactory(application)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}