package com.day.line.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.line.data.TaskRepository
import com.day.line.data.UserProfileRepository
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
import com.day.line.data.SettingsRepository
import com.day.line.analytics.AnalyticsManager

import com.day.line.data.DailyTaskCount
import java.util.Locale

data class GraphPoint(
    val label: String,
    val value: Int
)

data class ProfileUiState(
    val userName: String = "John Doe",
    val profileImageUri: String? = null,
    val streak: Int = 0,
    val completionRate: Int = 0,
    val tasksCreatedToday: Int = 0,
    val completedTasksToday: Int = 0,
    val activityData: List<GraphPoint> = emptyList(),
    val wakeUpTime: String? = "08:00"
)


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userProfileRepository: UserProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState
    
    val hasClickedTutorial: StateFlow<Boolean> = settingsRepository.hasClickedTutorial

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val sevenDaysAgoStr = today.minusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                combine(
                    userProfileRepository.getUserProfile(),
                    taskRepository.getTaskCountForDate(todayStr),
                    taskRepository.getCompletedTaskCountForDate(todayStr),
                    taskRepository.getDatesWithCompletedTasks(),
                    taskRepository.getTaskCountsByDate(sevenDaysAgoStr)
                ) { userProfile, totalTasks, completedTasks, completedDates, activityData ->
                    
                    // Format completion rate
                    val completionRate = if (totalTasks > 0) {
                        (completedTasks.toFloat() / totalTasks * 100).toInt()
                    } else {
                        0
                    }
                    
                    // Calculate streak
                    val streak = calculateStreak(completedDates)
                    
                    // Normalize activity data and map to GraphPoint
                    val fullWeekData = getPast7Days(today).map { dateStr ->
                        val count = activityData.find { it.date == dateStr }?.count ?: 0
                        
                        // Parse date for label
                        val label = try {
                            val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            date.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))
                        } catch (e: Exception) {
                            dateStr.take(3)
                        }
                        
                        GraphPoint(label, count)
                    }
                    
                    ProfileUiState(
                        userName = userProfile?.name ?: "John Doe",
                        profileImageUri = userProfile?.profileImageUri,
                        streak = streak,
                        completionRate = completionRate,
                        tasksCreatedToday = totalTasks,
                        completedTasksToday = completedTasks,
                        activityData = fullWeekData,
                        wakeUpTime = userProfile?.wakeUpTime
                    )
                }.collect { newState ->
                    _uiState.value = newState
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPast7Days(today: LocalDate): List<String> {
        return (0..6).map { i ->
            today.minusDays((6 - i).toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
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
        }.sortedDescending()

        if (dates.isEmpty()) return 0

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        
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
                continue
            } else {
                break
            }
        }
        
        return currentStreak
    }
    
    fun updateWakeUpTime(time: String) {
        viewModelScope.launch {
            try {
                userProfileRepository.updateWakeUpTime(time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateProfile(name: String, imageUri: String?) {
        viewModelScope.launch {
            try {
                userProfileRepository.updateProfile(name, imageUri)
                analyticsManager.logEditProfileClicked()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markTutorialClicked() {
        viewModelScope.launch {
            settingsRepository.setTutorialClicked()
            analyticsManager.logTutorialClicked()
        }
    }

    fun logScreenView(screenName: String) {
        analyticsManager.logScreenView(screenName)
    }
}
