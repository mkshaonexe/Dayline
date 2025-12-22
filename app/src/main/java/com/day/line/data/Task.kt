package com.day.line.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val title: String,

    val icon: String = "default", // Icon name (e.g., "Notifications", "List", "Face", "PlayArrow")
    
    val color: Int? = null, // Manual color override (ARGB)

    
    // Date in format: yyyy-MM-dd (e.g., "2025-12-19")
    val date: String,
    
    // Time in format: HH:mm (e.g., "14:30")
    val startTime: String,
    val endTime: String,
    
    val isAllDay: Boolean = false,
    
    val notes: String = "",
    
    // Subtasks stored as JSON string
    val subtasks: String = "[]",

    val isCompleted: Boolean = false,
    
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        
        fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
            return LocalDate.of(year, month, dayOfMonth).format(dateFormatter)
        }
        
        fun formatTime(hour: Int, minute: Int): String {
            return LocalTime.of(hour, minute).format(timeFormatter)
        }
    }
    
    fun getDisplayTime(): String {
        if (isAllDay) return "All Day"
        
        return try {
            val start = LocalTime.parse(startTime, timeFormatter)
            val end = LocalTime.parse(endTime, timeFormatter)
            val displayFormatter = DateTimeFormatter.ofPattern("h:mm a")
            "${start.format(displayFormatter)} - ${end.format(displayFormatter)}"
        } catch (e: Exception) {
            "$startTime - $endTime"
        }
    }
    
    fun getDisplayDate(): String {
        return try {
            val localDate = LocalDate.parse(date, dateFormatter)
            val displayFormatter = DateTimeFormatter.ofPattern("EEE, d. MMM yyyy")
            localDate.format(displayFormatter)
        } catch (e: Exception) {
            date
        }
    }
    
    fun getDuration(): String {
        if (isAllDay) return "All Day"
        
        return try {
            val start = LocalTime.parse(startTime, timeFormatter)
            val end = LocalTime.parse(endTime, timeFormatter)
            val minutes = java.time.Duration.between(start, end).toMinutes()
            val hours = minutes / 60
            val mins = minutes % 60
            
            when {
                hours > 0 && mins > 0 -> "${hours}h ${mins}m"
                hours > 0 -> "${hours}h"
                else -> "${mins}m"
            }
        } catch (e: Exception) {
            ""
        }
    }
}

data class DailyTaskCount(
    val date: String,
    val count: Int
)
