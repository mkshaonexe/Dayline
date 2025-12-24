package com.day.line

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.day.line.worker.UpdateWorker
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.installations.ktx.installations
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class DaylineApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase Analytics
        firebaseAnalytics = Firebase.analytics
        
        scheduleUpdateCheck()
        scheduleMisoWork()
        
        // Log Installation ID for In-App Messaging testing
        Firebase.installations.id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                android.util.Log.d("Installations", "Installation ID: " + task.result)
            } else {
                android.util.Log.e("Installations", "Unable to get Installation ID")
            }
        }
    }

    private fun scheduleUpdateCheck() {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()
            
        val updateWorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
            
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UpdateCheck",
            ExistingPeriodicWorkPolicy.KEEP,
            updateWorkRequest
        )
    }

    private fun scheduleMisoWork() {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.NOT_REQUIRED)
            .build()
            
        // Check every 2 hours
        val misoWorkRequest = PeriodicWorkRequestBuilder<com.day.line.worker.MisoWorker>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
            
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MisoWork",
            ExistingPeriodicWorkPolicy.KEEP,
            misoWorkRequest
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
