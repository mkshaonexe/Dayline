package com.day.line.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.SoftGray
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarStrip(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = try {
        LocalDate.parse(selectedDate, formatter)
    } catch (e: Exception) {
        LocalDate.of(2025, 12, 19)
    }

    // "Week containing the selected date" logic
    val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }
    
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background) // Ensure background consistent
    ) {
        // Month Year Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = currentDate.format(monthYearFormatter),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            // Optional: Calendar Icon or Today Button
             Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select Date",
                tint = DaylineOrange,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Days Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekDates.forEach { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayOfMonth = date.dayOfMonth.toString()
                val isSelected = date.isEqual(currentDate)

                DayItem(
                    day = dayName,
                    date = dayOfMonth,
                    isSelected = isSelected,
                    onClick = {
                        onDateSelected(date.format(formatter))
                    }
                )
            }
        }
    }
}

@Composable
fun DayItem(
    day: String, 
    date: String, 
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(40.dp)
            .clickableWithoutRipple { onClick() }
    ) {
        // Day Name (Mon, Tue)
        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) DaylineOrange else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Date Number (15, 16)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) DaylineOrange else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier {
    return this.then(
        clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarStripPreview() {
    CalendarStrip(
        selectedDate = "2025-12-19",
        onDateSelected = {}
    )
}
