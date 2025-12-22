package com.day.line.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.line.data.JournalEntry
import com.day.line.data.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: JournalRepository
) : ViewModel() {

    val journalEntries: StateFlow<List<JournalEntry>> = repository.allEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        checkAndAddDemoEntries()
    }

    private fun checkAndAddDemoEntries() {
        viewModelScope.launch {
            if (repository.getEntryCount() == 0) {
                // Add demo entries
                val demoEntries = listOf(
                    JournalEntry(
                        title = "Admission War 📚😤",
                        content = "Man, Physics is killing me today! 🤯 Cycles of karnot engine... whyyy? 😫 Need to grind harder for BUET. Sleep is for the weak! ☕️💀 #AdmissionLife #EngineeringDream",
                        timestamp = System.currentTimeMillis()
                    ),
                    JournalEntry(
                        title = "Vibe Check ✨🎧",
                        content = "Just chilling with the squad today. 🍔🍟 Gossip session was wild! 😂 Also, that new song is on repeat. 🎶 Mood: Unbothered. 😎",
                        timestamp = System.currentTimeMillis() - 86400000 // Yesterday
                    ),
                    JournalEntry(
                        title = "Engineering Struggles 🇧🇩🔧",
                        content = "Udvash exam was tough... 📉 math portion chilo impossible type er. 😭 But gotta keep pushing. My parents have high hopes. 🥺 Need to solve more Question Banks. 😫🙏 #BUETDream",
                        timestamp = System.currentTimeMillis() - 172800000 // 2 Days ago
                    ),
                    JournalEntry(
                        title = "Life Lately 🍃",
                        content = "Feeling a bit overwhelmed tbh. 🫠 balancing coaching, college, and life is hard. But found a cute cat on the street today! 🐈💖 Small joys. ✨",
                        timestamp = System.currentTimeMillis() - 259200000 // 3 Days ago
                    )
                )

                demoEntries.forEach { repository.addEntry(it) }
            }
        }
    }

    fun addEntry(title: String, content: String, moodColor: Int = -1) {
        viewModelScope.launch {
            repository.addEntry(
                JournalEntry(
                    title = title,
                    content = content,
                    moodColor = moodColor
                )
            )
        }
    }

    fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }
}
