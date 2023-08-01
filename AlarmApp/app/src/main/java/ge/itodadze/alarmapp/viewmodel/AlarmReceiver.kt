package ge.itodadze.alarmapp.viewmodel

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ge.itodadze.alarmapp.R
import ge.itodadze.alarmapp.view.activity.MainActivity
import ge.itodadze.alarmapp.viewmodel.model.AlarmItem

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        const val ALARM_ACTION: String = "ge.itodadze.alarmapp.ALARM_ACTION"
        const val SNOOZE_ACTION: String = "ge.itodadze.alarmapp.SNOOZE_ACTION"
        const val CANCEL_ACTION: String = "ge.itodadze.alarmapp.CANCEL_ACTION"
        const val CHANNEL: String = "ge.itodadze.alarmapp.CHANNEL"
        const val TITLE: String = "Alarm Message!"
        const val CONTENT: String = "Alarm set on "
        const val SNOOZE_TEXT: String = "SNOOZE"
        const val CANCEL_TEXT: String = "CANCEL"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            p1?.let {
                val alarm: AlarmItem = p1.getStringExtra(AlarmItem.ITEM_IDENTIFIER)
                    ?.let { AlarmItem.deserialize(it) } ?: return
                val requestCode = alarm.hashCode()
                val manager = NotificationManagerCompat.from(p0)
                when (p1.action) {
                    ALARM_ACTION -> alarm(p0, manager, alarm, requestCode)
                    SNOOZE_ACTION -> snooze(p0, manager, alarm, requestCode)
                    CANCEL_ACTION -> cancel(manager, requestCode)
                    else -> { return }
                }
            }
        }
    }

    private fun alarm(context: Context, manager: NotificationManagerCompat
                      , alarm: AlarmItem, requestCode: Int) {
        val notification = makeNotification(context, alarm, requestCode, manager)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        manager.notify(requestCode, notification)
    }

    private fun snooze(context: Context, manager: NotificationManagerCompat,
                       alarm: AlarmItem, requestCode: Int) {
        manager.cancel(requestCode)
        val notification = makeNotification(context, alarm, requestCode, manager)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@postDelayed
                }
                manager.notify(requestCode, notification)
            }, 60000
        )
    }

    private fun cancel(manager: NotificationManagerCompat, requestCode: Int) {
        manager.cancel(requestCode)
    }

    private fun makeNotification(context: Context, alarm: AlarmItem, requestCode: Int,
                                 manager: NotificationManagerCompat): Notification {
        val mainNotification = PendingIntent.getActivity(
            context,
            requestCode,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val snoozeClick = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(SNOOZE_ACTION).apply {
                putExtra(AlarmItem.ITEM_IDENTIFIER, alarm.serialize())
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val cancelClick = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(CANCEL_ACTION).apply {
                putExtra(AlarmItem.ITEM_IDENTIFIER, alarm.serialize())
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL, "alarm_channel", NotificationManager.IMPORTANCE_HIGH)
        )
        return NotificationCompat.Builder(context, CHANNEL)
            .setSmallIcon(R.drawable.alarm_clock)
            .setContentTitle(TITLE)
            .setContentText(CONTENT + alarm.time)
            .setContentIntent(mainNotification)
            .addAction(0, CANCEL_TEXT, cancelClick)
            .addAction(0, SNOOZE_TEXT, snoozeClick)
            .build()
    }

}