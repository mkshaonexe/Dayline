package com.day.line.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.line.data.Task
import com.day.line.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import com.day.line.ui.notification.NotificationScheduler
import com.day.line.analytics.AnalyticsManager

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val notificationScheduler: NotificationScheduler,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    
    // Current selected date (default: Today)
    private val _selectedDate = MutableStateFlow(
        LocalDate.now().format(dateFormatter)
    )

    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()
    
    // Tasks for the selected date
    // Sealed interface for Timeline Items
    sealed interface TimelineItem {
        data class Fixed(
            val time: String,
            val title: String,
            val subtitle: String,
            val iconName: String,
            val colorHex: Long,
            val isCompleted: Boolean = false
        ) : TimelineItem

        data class UserTask(val task: Task) : TimelineItem
    }

    private val _completedFixedItems = MutableStateFlow<Set<String>>(emptySet())

    @OptIn(ExperimentalCoroutinesApi::class)
    val timelineItems: StateFlow<List<TimelineItem>> = _selectedDate
        .flatMapLatest { date ->
            repository.getTasksForDate(date).map { tasks -> date to tasks }
        }
        .combine(_completedFixedItems) { (date, tasks), completedFixed ->
            val fixedItems = listOf(
                TimelineItem.Fixed(
                    time = "05:30",
                    title = "Wake up!",
                    subtitle = "A well-deserved break.",
                    iconName = "Notifications",
                    colorHex = 0xFFFF8A80, // PastelRed
                    isCompleted = completedFixed.contains("${date}_Wake up!")
                ),
                TimelineItem.Fixed(
                    time = "23:00",
                    title = "Wind Down",
                    subtitle = "Time to recharge.",
                    iconName = "Bedtime",
                    colorHex = 0xFF9575CD, // DeepPurple200
                    isCompleted = completedFixed.contains("${date}_Wind Down")
                )
            )

            val userItems = tasks.map { TimelineItem.UserTask(it) }
            val userTitles = tasks.map { it.title }.toSet()

            val filteredFixedItems = fixedItems.filter { fixed ->
                !userTitles.contains(fixed.title)
            }
            
            (filteredFixedItems + userItems).sortedBy { item ->
                when (item) {
                    is TimelineItem.Fixed -> item.time
                    is TimelineItem.UserTask -> item.task.startTime
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFixedItemCompletion(date: String, title: String) {
        val key = "${date}_$title"
        val isNowCompleted = !_completedFixedItems.value.contains(key)
        _completedFixedItems.value = if (_completedFixedItems.value.contains(key)) {
            _completedFixedItems.value - key
        } else {
            _completedFixedItems.value + key
        }
        
        if (isNowCompleted) {
            analyticsManager.logTaskCompleted(isUserTask = false)
        } else {
            analyticsManager.logTaskUncompleted(isUserTask = false)
        }
    }
    
    fun selectDate(date: String) {
        _selectedDate.value = date
    }
    
    fun selectDate(year: Int, month: Int, dayOfMonth: Int) {
        _selectedDate.value = Task.formatDate(year, month, dayOfMonth)
    }
    
    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            notificationScheduler.scheduleNotification(task)
            analyticsManager.logTaskCreated(
                hasTime = task.startTime != "00:00", 
                hasIcon = task.icon != "Star"
            )
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            notificationScheduler.scheduleNotification(task)
            // Ideally check for completion change here, but for now generic log or rely on other signals if strictly needed.
            // Keeping it simple as per implementation plan to avoid complex refactoring.
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            notificationScheduler.cancelNotification(task)
            analyticsManager.logTaskDeleted()
        }
    }
    
    fun deleteTaskById(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
            analyticsManager.logTaskDeleted()
        }
    }
    
    fun getDisplayDate(): String {
        return try {
            val date = LocalDate.parse(_selectedDate.value, dateFormatter)
            val displayFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
            date.format(displayFormatter)
        } catch (e: Exception) {
            _selectedDate.value
        }
    }
    
    fun getTodayDate(): String {
        return LocalDate.now().format(dateFormatter)
    }
}
