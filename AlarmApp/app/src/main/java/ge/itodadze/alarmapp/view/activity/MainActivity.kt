package ge.itodadze.alarmapp.view.activity

import android.app.TimePickerDialog
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import ge.itodadze.alarmapp.R
import ge.itodadze.alarmapp.databinding.ActivityMainBinding
import ge.itodadze.alarmapp.view.adapter.AlarmsListAdapter
import ge.itodadze.alarmapp.view.listener.AlarmItemListener
import ge.itodadze.alarmapp.viewmodel.AlarmReceiver
import ge.itodadze.alarmapp.viewmodel.MainViewModel
import ge.itodadze.alarmapp.viewmodel.model.AlarmItem
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, AlarmItemListener {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.getMainViewModelFactory(
            application
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerAdapters()

        registerObservers()

        registerListeners()

        registerReceiver(AlarmReceiver(), IntentFilter().apply{
            addAction(AlarmReceiver.ALARM_ACTION)
            addAction(AlarmReceiver.SNOOZE_ACTION)
            addAction(AlarmReceiver.CANCEL_ACTION)
        })
    }

    private fun registerAdapters() {
        binding.alarms.adapter = AlarmsListAdapter(this, ArrayList())
    }

    private fun registerObservers() {
        viewModel.mode.observe(this) {
            adjustMode(it!!)
        }

        viewModel.alarms.observe(this) {
            (binding.alarms.adapter as AlarmsListAdapter).update(it)
        }
    }

    private fun registerListeners() {
        binding.switchModeText.setOnClickListener{
            viewModel.switchMode()
        }
        binding.addIcon.setOnClickListener{
            chooseTime()
        }
    }

    private fun adjustMode(mode: Boolean) {
        if (mode) {
            binding.switchModeText.text = resources.getString(
                R.string.switch_text, resources.getString(R.string.light)
            )
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            binding.switchModeText.text = resources.getString(
                R.string.switch_text, resources.getString(R.string.dark)
            )
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun chooseTime() {
        val calendar: Calendar = Calendar.getInstance()
        val dialog = TimePickerDialog(
            this,
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        dialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if (!viewModel.addAlarm(p1, p2)) {
            Toast.makeText(applicationContext, "Adding alarm failed.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun alarmChecked(alarm: AlarmItem, status: Boolean) {
        viewModel.changeStatus(alarm, status)
    }

    override fun alarmLongClicked(alarm: AlarmItem) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Are you sure you want to delete item?")
            .setPositiveButton("Yes"){
                dialog, _ ->
                viewModel.deleteAlarm(alarm)
                dialog?.dismiss()
            }.setNegativeButton("No"){
                dialog, _ ->
                dialog?.dismiss()
            }
        dialog.show()
    }

}