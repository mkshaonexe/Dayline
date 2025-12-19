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
import com.day.line.ui.theme.DaylineOrange

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Settings",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SettingsSection("Preferences") {
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Dark Mode",
                hasSwitch = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                hasSwitch = true
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsSection("About") {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version 0.1.0",
                subtitle = "Build 234"
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
            color = Color.Gray,
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
    hasSwitch: Boolean = false
) {
    var isChecked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if(hasSwitch) isChecked = !isChecked },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    color = Color.Black
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            if (hasSwitch) {
                Switch(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
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
                    tint = Color.LightGray
                )
            }
        }
    }
}
