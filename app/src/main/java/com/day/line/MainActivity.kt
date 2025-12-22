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

    @javax.inject.Inject
    lateinit var updateManager: com.day.line.data.update.UpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by settingsRepository.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())
            val themeColorName by settingsRepository.themeColor.collectAsState(initial = "Orange")
            val accentColor = ThemeColor.fromName(themeColorName).color
            
            // Check Update Status
            var updateState by androidx.compose.runtime.remember { 
                androidx.compose.runtime.mutableStateOf<com.day.line.data.update.UpdateManager.UpdateState>(com.day.line.data.update.UpdateManager.UpdateState.NoUpdate) 
            }
            
            LaunchedEffect(Unit) {
                 val status = updateManager.checkUpdateState()
                 if (status is com.day.line.data.update.UpdateManager.UpdateState.ForcedUpdate) {
                     updateState = status
                 }
                 
                 // Firebase ID log
                 FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       Log.d("DaylineID", "FID: ${task.result}")
                   }
               }
            }

            // Request Notification Permission on Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { }
                
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            DaylineTheme(darkTheme = isDarkTheme, accentColor = accentColor) {
                if (updateState is com.day.line.data.update.UpdateManager.UpdateState.ForcedUpdate) {
                    val forced = updateState as com.day.line.data.update.UpdateManager.UpdateState.ForcedUpdate
                    com.day.line.ui.update.ForceUpdateScreen(
                        downloadUrl = forced.version.downloadUrl,
                        onContactSupport = {
                            val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                                data = android.net.Uri.parse("mailto:")
                                putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("mkshaonnew31@gmail.com"))
                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Dayline app stop working ask for the update")
                                putExtra(android.content.Intent.EXTRA_TEXT, "The app has stopped working because it requires an update. Please help me update.")
                            }
                            startActivity(intent)
                        }
                    )
                } else {
                    HomeScreen()
                }
            }
        }
    }
}