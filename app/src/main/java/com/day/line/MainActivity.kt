package com.day.line

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.day.line.ui.home.HomeScreen
import com.day.line.ui.theme.DaylineTheme
import com.day.line.ui.theme.ThemeColor
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.installations.FirebaseInstallations
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @javax.inject.Inject
    lateinit var settingsRepository: com.day.line.data.SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by settingsRepository.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())
            val themeColorName by settingsRepository.themeColor.collectAsState(initial = "Orange")
            val accentColor = ThemeColor.fromName(themeColorName).color
            
            // Debug: Get Firebase Installation ID
            LaunchedEffect(Unit) {
               FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       val token = task.result
                       Log.d("DaylineID", "Firebase Install ID: $token")
                       // Toast.makeText(applicationContext, "FID: $token", Toast.LENGTH_LONG).show() 
                   } else {
                       Log.e("DaylineID", "Unable to get Installation ID")
                   }
               }
            }

            // Request Notification Permission on Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    // Handle permission result if needed
                }
                
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            DaylineTheme(darkTheme = isDarkTheme, accentColor = accentColor) {
                HomeScreen()
            }
        }
    }
}