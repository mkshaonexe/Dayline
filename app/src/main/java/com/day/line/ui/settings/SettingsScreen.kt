package com.day.line.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ImportantDevices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.LaunchedEffect
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
    val themeColor by viewModel.themeColor.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val hasClickedEarlyAccess by viewModel.hasClickedEarlyAccess.collectAsState()
    val context = LocalContext.current
    var showColorPicker by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    
    // Update Dialog
    if (updateStatus is UpdateStatus.Available) {
        val version = (updateStatus as UpdateStatus.Available).version
        UpdateDialog(
            version = version,
            onDismiss = { viewModel.clearUpdateStatus() },
            onUpdateViaPlayStore = { 
                val url = version.downloadUrl ?: "https://play.google.com/store/apps/details?id=${context.packageName}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
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
        
        SettingsSection("Early Access") {
            SettingsItem(
                icon = Icons.Default.Shop,
                title = "Install from Play Store",
                subtitle = "Get Early Access",
                showBadge = !hasClickedEarlyAccess,
                onClick = { 
                    viewModel.markEarlyAccessClicked()
                    showFeedbackDialog = true 
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection("Community") {
            SettingsItem(
                icon = Icons.Default.Group,
                title = "Public Group",
                subtitle = "Join our Telegram community",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/appdayline"))
                    try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingsItem(
                icon = Icons.Default.Apps,
                title = "Other Apps",
                subtitle = "Check out my other apps",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mkshaon.com/all_app"))
                    try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection("About") {
            SettingsItemWithReloadButton(
                icon = Icons.Default.Info,
                title = "Version ${com.day.line.BuildConfig.VERSION_NAME}",
                subtitle = "Check for updates",
                isLoading = updateStatus is UpdateStatus.Checking,
                onReloadClick = { viewModel.checkForUpdate() }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingsItem(
                icon = Icons.Default.Email,
                title = "Contact Support",
                subtitle = "DM for issues",
                onClick = {
                    val subject = Uri.encode("Dayline app issu /support/ bug report")
                    val body = Uri.encode("Write here the problem \n\n Thank you")
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:mkshaonnew31@gmail.com?subject=$subject&body=$body")
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
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mkshaonexe/Dayline/blob/main/PRIVACY_POLICY.md"))
                    try { context.startActivity(intent) } catch (e: Exception) { e.printStackTrace() }
                }
            )
        }
    }
    
    // Color Picker Dialog
    if (showColorPicker) {
        ColorPickerDialog(
            currentColor = themeColor,
            onDismiss = { showColorPicker = false },
            onColorSelected = { colorName ->
                viewModel.setThemeColor(colorName)
            }
        )
    }

    // Feedback Dialog
    if (showFeedbackDialog) {
        FeedbackDialog(onDismiss = { showFeedbackDialog = false })
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
    showBadge: Boolean = false,
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
            
            if (showBadge) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
                Spacer(modifier = Modifier.size(8.dp))
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
fun SettingsItemWithReloadButton(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isLoading: Boolean = false,
    onReloadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = DaylineOrange,
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(onClick = onReloadClick) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Check for updates",
                        tint = DaylineOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
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
        title = { Text(text = "Update Available: ${version.versionName}") },
        text = {
            Column {
                Text(version.changelog ?: "New improvements available.")
                Spacer(modifier = Modifier.height(16.dp))
                Text("You can download the update directly or visit our Telegram channel.")
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = onUpdateViaPlayStore,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = DaylineOrange)
            ) {
                Text("Download Update")
            }
        },
        dismissButton = {
            Row {
                androidx.compose.material3.OutlinedButton(
                    onClick = onUpdateViaTelegram
                ) {
                    Text("Telegram")
                }
                Spacer(modifier = Modifier.size(8.dp))
                androidx.compose.material3.TextButton(onClick = onDismiss) {
                    Text("Later")
                }
            }
        }
    )
}


@Composable
fun SettingsItemWithColorPreview(
    icon: ImageVector,
    title: String,
    subtitle: String,
    currentColor: Color,
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
                tint = currentColor,
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
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(currentColor)
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
fun FeedbackDialog(
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false // Full screen
        )
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "Early Access Feedback",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Track Screen View
                // WebView
                androidx.compose.ui.viewinterop.AndroidView(
                    factory = { context ->
                        android.webkit.WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            webViewClient = android.webkit.WebViewClient()
                            loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSeTXwKnATK_XMpsBtrWH1h-FBwRxJrt63MeRAZBw61qL2w7Gg/viewform?embedded=true")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
