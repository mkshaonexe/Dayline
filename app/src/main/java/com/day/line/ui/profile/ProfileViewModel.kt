package com.day.line.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.line.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class ProfileUiState(
    val streak: Int = 0,
    val completionRate: Int = 0,
    val tasksCreatedToday: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            combine(
                taskRepository.getTaskCountForDate(today),
                taskRepository.getCompletedTaskCountForDate(today),
                taskRepository.getDatesWithCompletedTasks()
            ) { totalTasks, completedTasks, completedDates ->
                val completionRate = if (totalTasks > 0) {
                    (completedTasks.toFloat() / totalTasks * 100).toInt()
                } else {
                    0
                }
                
                val streak = calculateStreak(completedDates)
                
                ProfileUiState(
                    streak = streak,
                    completionRate = completionRate,
                    tasksCreatedToday = totalTasks
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun calculateStreak(completedDates: List<String>): Int {
        if (completedDates.isEmpty()) return 0
        
        val dates = completedDates.mapNotNull { 
            try {
                LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: Exception) {
                null
            }
        }.sortedDescending() // Ensure sorted, though query does it too

        if (dates.isEmpty()) return 0

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        
        // Check if streak is active (completed today or yesterday)
        val firstDate = dates.first()
        if (!firstDate.isEqual(today) && !firstDate.isEqual(yesterday)) {
            return 0
        }

        var currentStreak = 1
        var previousDate = firstDate

        for (i in 1 until dates.size) {
            val date = dates[i]
            if (date.isEqual(previousDate.minusDays(1))) {
                currentStreak++
                previousDate = date
            } else if (date.isEqual(previousDate)) {
                // Duplicate date, ignore
                continue
            } else {
                // Streak broken
                break
            }
        }
        
        return currentStreak
    }
}
