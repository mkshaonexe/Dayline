package com.day.line.ui.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.day.line.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("TASK_ID", -1)
        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"
        val isPreReminder = intent.getBooleanExtra("IS_PRE_REMINDER", false)
        
        if (taskId != -1L) {
             val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             
             val (title, message) = if (isPreReminder) {
                 "Wind Down Reminder" to "Your wind down is in 30 min - lower your screen time"
             } else {
                 "Task Reminder" to taskTitle
             }
             
             val notification = NotificationCompat.Builder(context, "task_reminders")
                 .setSmallIcon(R.mipmap.ic_launcher_round)
                 .setLargeIcon(android.graphics.BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                 .setContentTitle(title)
                 .setContentText(message)
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                 .setAutoCancel(true)
                 .build()

             notificationManager.notify(taskId.toInt(), notification)
        }
    }
}
