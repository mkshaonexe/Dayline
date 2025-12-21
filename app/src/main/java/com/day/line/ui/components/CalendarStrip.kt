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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.day.line.ui.theme.*
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
    val currentStateDate = try {
        LocalDate.parse(selectedDate, formatter)
    } catch (e: Exception) {
        LocalDate.of(2025, 12, 19)
    }

    // Show 3 days before and 3 days after the selected date (centered view)
    val startDate = currentStateDate.minusDays(3)
    
    val weekDates = (0..6).map { startDate.plusDays(it.toLong()) }
    
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    val haptic = LocalHapticFeedback.current
    var totalDrag by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                var initialDate: LocalDate = currentStateDate
                var lastDayOffset = 0
                
                detectHorizontalDragGestures(
                    onDragStart = { 
                        totalDrag = 0f 
                        lastDayOffset = 0
                        initialDate = currentStateDate
                    },
                    onDragEnd = { 
                        totalDrag = 0f 
                        lastDayOffset = 0
                    }
                ) { change, dragAmount ->
                    change.consume()
                    totalDrag += dragAmount
                    
                    val threshold = 100f
                    // Calculate how many days we have moved based on total drag
                    // Swipe Right (>0) -> Previous Day (-1)
                    // Swipe Left (<0) -> Next Day (+1)
                    val currentDayOffset = -(totalDrag / threshold).toInt()
                    
                    if (currentDayOffset != lastDayOffset) {
                        val newDate = initialDate.plusDays(currentDayOffset.toLong())
                        onDateSelected(newDate.format(formatter))
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        lastDayOffset = currentDayOffset
                    }
                }
            }
            .padding(vertical = 24.dp, horizontal = 16.dp) // Balanced vertical padding
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Capitalize first letter logic is handled naturally by the formatter "MMMM yyyy" (e.g. December 2025)
            // But just in case, we use the default output which is Title Case for months in English.
            Text(
                text = currentStateDate.format(monthYearFormatter), 
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp), // Balanced Title Size
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 0.5.sp 
            )
            
            // Icons removed as per user request
        }
        
        // Days Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), 
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekDates.forEach { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayOfMonth = date.dayOfMonth.toString()
                val isSelected = date.isEqual(currentStateDate)

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
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), // Smaller, cleaner day name
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(40.dp) // Balanced circle size
                .background(
                    if (isSelected) DaylinePink else Color.Transparent, 
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
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp // Balanced date size
            )
        }
        
        // Task Indicator Dots
        Row(modifier = Modifier.padding(top = 6.dp)) {
            if (isSelected || date.toInt() % 2 != 0) { // Dummy logic: show dots for selected or odd days
                 Box(modifier = Modifier.size(4.dp).background(PastelRed, CircleShape))
                 if (date.toInt() % 3 == 0) {
                     androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(2.dp))
                     Box(modifier = Modifier.size(4.dp).background(PastelBlue, CircleShape))
                 }
            } else {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
            }
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
