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
        
        if (taskId != -1L) {
             val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             
             // Ensure channel exists (id: "task_reminders")
             // Note: Channel creation is best done in App or Helper, but we can double check here or trust it's done.
             
             val notification = NotificationCompat.Builder(context, "task_reminders")
                 .setSmallIcon(R.mipmap.ic_launcher_round) // Replace with valid icon
                 .setContentTitle("Task Reminder")
                 .setContentText(taskTitle)
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                 .setAutoCancel(true)
                 .build()

             notificationManager.notify(taskId.toInt(), notification)
        }
    }
}
