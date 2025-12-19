package com.day.line.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.day.line.ui.theme.DaylineOrange

@Composable
fun CalendarStrip() {
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
                text = "December 2025",
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
            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            val dates = listOf("15", "16", "17", "18", "19", "20", "21")
            
            days.forEachIndexed { index, day ->
                DayItem(
                    day = day,
                    date = dates[index],
                    isSelected = index == 3 // Dummy selection
                )
            }
        }
    }
}

@Composable
fun DayItem(day: String, date: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
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
                    if (isSelected) Color.Black else Color.Transparent, // Using Black for selected day based on image
                    CircleShape
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

@Preview(showBackground = true)
@Composable
fun CalendarStripPreview() {
    CalendarStrip()
}
