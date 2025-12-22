package com.day.line.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.day.line.ui.theme.DaylineOrange

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val context = LocalContext.current
    
    // Manage Permissions (Notification & Alarms)
    // Note: For simplicity, we are checking current status. Real-time updates might require observing lifecycle or composition updates.
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var hasAlarmPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.canScheduleExactAlarms()
            } else {
                true
            }
        )
    }

    // Permission Launchers
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    )

    // Update Dialog
    if (updateStatus is UpdateStatus.Available) {
        val version = (updateStatus as UpdateStatus.Available).version
        UpdateDialog(
            version = version,
            onDismiss = { viewModel.clearUpdateStatus() },
            onUpdateViaPlayStore = { 
                // Placeholder for Play Store intent or download link if applicable
                // For now, maybe just open the telegram or a generic link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
                try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
            },
            onUpdateViaTelegram = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/appdayline"))
                try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
            }
        )
    } else if (updateStatus is UpdateStatus.UpToDate) {
        // Optional: Show a small toast or snackbar
        // Toast.makeText(context, "App is up to date", Toast.LENGTH_SHORT).show()
        // Reset status immediately to avoid persistent dialog/state
        viewModel.clearUpdateStatus()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Settings",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SettingsSection("Preferences") {
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Dark Mode",
                hasSwitch = true,
                isChecked = isDarkTheme,
                onCheckedChange = { viewModel.toggleTheme(it) }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection("Permissions") {
            // Notification Permission
            SettingsActionItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                statusText = if (hasNotificationPermission) "Granted" else "Grant",
                statusColor = if (hasNotificationPermission) DaylineOrange else MaterialTheme.colorScheme.error,
                onClick = {
                    if (!hasNotificationPermission) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Exact Alarm Permission
            SettingsActionItem(
                icon = Icons.Default.Schedule,
                title = "Alarms & Reminders",
                statusText = if (hasAlarmPermission) "Granted" else "Grant",
                statusColor = if (hasAlarmPermission) DaylineOrange else MaterialTheme.colorScheme.error,
                onClick = {
                    if (!hasAlarmPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        context.startActivity(intent)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection("About") {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version ${com.day.line.BuildConfig.VERSION_NAME}",
                subtitle = "Check for updates",
                onClick = { viewModel.checkForUpdate() }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingsItem(
                icon = Icons.Default.Email,
                title = "Contact Support",
                subtitle = "DM for issues",
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:mkshaonnew31@gmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "Dayline App Issue / Feature Request")
                        putExtra(Intent.EXTRA_TEXT, "Describe here all the features you want in this app and the issue you found\n\n\nThank you")
                    }
                    try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Privacy Policy",
                subtitle = "View our privacy policy",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/YOUR_USERNAME/Dayline/blob/main/PRIVACY_POLICY.md"))
                    try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
                }
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant, 
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    hasSwitch: Boolean = false,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                if (hasSwitch) onCheckedChange(!isChecked) else onClick() 
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DaylineOrange,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.size(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (hasSwitch) {
                Switch(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = DaylineOrange,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SettingsActionItem(
    icon: ImageVector,
    title: String,
    statusText: String,
    statusColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DaylineOrange,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.size(16.dp))
            
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            Text(
                text = statusText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            
            Spacer(modifier = Modifier.size(8.dp))
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UpdateDialog(
    version: com.day.line.data.update.AppVersion,
    onDismiss: () -> Unit,
    onUpdateViaPlayStore: () -> Unit,
    onUpdateViaTelegram: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Update Available") },
        text = {
            Column {
                Text("Version ${version.versionName} is available.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Changelog: ${version.changelog}")
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = onUpdateViaPlayStore,
                 colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = DaylineOrange)
            ) {
                Text("Play Store")
            }
        },
        dismissButton = {
             androidx.compose.material3.OutlinedButton(
                onClick = onUpdateViaTelegram
            ) {
                Text("Telegram")
            }
        }
    )
}
