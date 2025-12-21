package com.day.line.ui.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.day.line.data.Task
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Reminders"
            val descriptionText = "Notifications for scheduled tasks"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("task_reminders", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(task: Task) {
        if (task.startTime.isEmpty() || task.date.isEmpty()) return

        try {
            // Parse time and date string "HH:mm", "yyyy-MM-dd"
            val localDateTime = LocalDateTime.parse("${task.date}T${task.startTime}")
            val zoneId = ZoneId.systemDefault()
            val triggerTime = localDateTime.atZone(zoneId).toInstant().toEpochMilli()

            // Check if this is a "Wind Down" task
            val isWindDown = task.title.contains("Wind Down", ignoreCase = true)
            
            // Schedule the main notification
            if (triggerTime > System.currentTimeMillis()) {
                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    putExtra("TASK_ID", task.id)
                    putExtra("TASK_TITLE", task.title)
                    putExtra("IS_PRE_REMINDER", false)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    task.id.toInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
                
                // For Wind Down tasks, schedule an additional reminder 30 minutes before
                if (isWindDown) {
                    val preReminderTime = triggerTime - (30 * 60 * 1000) // 30 minutes before
                    
                    if (preReminderTime > System.currentTimeMillis()) {
                        val preIntent = Intent(context, NotificationReceiver::class.java).apply {
                            putExtra("TASK_ID", task.id)
                            putExtra("TASK_TITLE", task.title)
                            putExtra("IS_PRE_REMINDER", true)
                        }

                        val prePendingIntent = PendingIntent.getBroadcast(
                            context,
                            (task.id.toInt() + 1000000), // Different ID for pre-reminder
                            preIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )

                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            preReminderTime,
                            prePendingIntent
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun cancelNotification(task: Task) {
        // Cancel main notification
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        
        // Cancel pre-reminder if it exists (for Wind Down tasks)
        val preIntent = Intent(context, NotificationReceiver::class.java)
        val prePendingIntent = PendingIntent.getBroadcast(
            context,
            (task.id.toInt() + 1000000),
            preIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(prePendingIntent)
    }
}
