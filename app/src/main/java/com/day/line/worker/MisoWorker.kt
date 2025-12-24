package com.day.line.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.day.line.MainActivity
import com.day.line.R
import com.day.line.data.TaskDao
import com.day.line.data.UserProfileDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.first

@HiltWorker
class MisoWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskDao: TaskDao,
    private val userProfileDao: UserProfileDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val userProfile = userProfileDao.getUserProfileSync() ?: return Result.success()
            val wakeUpTimeStr = userProfile.wakeUpTime ?: return Result.success()

            val wakeUpTime = LocalTime.parse(wakeUpTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            val currentTime = LocalTime.now()

            // Only trigger if current time is after wake up time
            if (currentTime.isAfter(wakeUpTime)) {
                val todayDate = LocalDate.now().toString() // Format: YYYY-MM-DD (default)
                // We need to match existing date format in DB.
                // TaskDao.kt shows: "yyyy-MM-dd" in NotificationScheduler. Logic usually assumes standard ISO or similar.
                // Let's assume standard format derived from LocalDate.toString() or similar used elsewhere.
                // I'll check how tasks are saved, but usually "yyyy-MM-dd" is standard.
                // Assuming "yyyy-MM-dd"
                
                val taskCount = taskDao.getTaskCountForDate(todayDate).first()

                if (taskCount == 0) {
                    showMisoNotification(
                        title = "Oi! Amare vuila gesos? 😤",
                        message = "Taile TODO banao nai keno! 😡 Taratari plan kor!"
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }

        return Result.success()
    }

    private fun showMisoNotification(title: String, message: String) {
        val channelId = "miso_channel"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Miso AI"
            val descriptionText = "Notifications from Miso AI"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
             if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(2001, builder.build())
            }
        }
    }
}
