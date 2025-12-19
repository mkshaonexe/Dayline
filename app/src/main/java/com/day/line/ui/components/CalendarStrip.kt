package com.day.line.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    // Calculate start of the week (Monday)
    // If we want a scrolling calendar later, we'd need more logic. 
    // For now, let's just show the week surrounding the selected date
    // or specifically the current week. 
    // Let's stick to "Week containing the selected date" logic for now.
    val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
    
    val weekDates = (0..6).map { startOfWeek.plusDays(it.toLong()) }
    
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentDate.format(monthYearFormatter),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            // Icon placeholder
        }
        
        // Days Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
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
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(36.dp)
                .background(
                    if (isSelected) Color.Black else Color.Transparent,
                    CircleShape
                )
                .then(
                    if (!isSelected) {
                        Modifier.clickableWithoutRipple { onClick() }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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
