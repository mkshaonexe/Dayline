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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    
    // Current selected date (default: Dec 19, 2025)
    private val _selectedDate = MutableStateFlow(
        LocalDate.of(2025, 12, 19).format(dateFormatter)
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
            val colorHex: Long
        ) : TimelineItem

        data class UserTask(val task: Task) : TimelineItem
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val timelineItems: StateFlow<List<TimelineItem>> = _selectedDate
        .flatMapLatest { date ->
            repository.getTasksForDate(date)
        }
        .map { tasks ->
            val fixedItems = listOf(
                TimelineItem.Fixed(
                    time = "05:30",
                    title = "Wake up!",
                    subtitle = "A well-deserved break.",
                    iconName = "Notifications",
                    colorHex = 0xFFFF8A80 // PastelRed
                ),
                TimelineItem.Fixed(
                    time = "23:00",
                    title = "Wind Down",
                    subtitle = "Time to recharge.",
                    iconName = "Bedtime", // Ensure we map this or use a default
                    colorHex = 0xFF9575CD // DeepPurple200 (approx for sleep)
                )
            )

            val userItems = tasks.map { TimelineItem.UserTask(it) }
            
            (fixedItems + userItems).sortedBy { item ->
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
    
    fun selectDate(date: String) {
        _selectedDate.value = date
    }
    
    fun selectDate(year: Int, month: Int, dayOfMonth: Int) {
        _selectedDate.value = Task.formatDate(year, month, dayOfMonth)
    }
    
    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
    
    fun deleteTaskById(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
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
        return LocalDate.of(2025, 12, 19).format(dateFormatter)
    }
}
