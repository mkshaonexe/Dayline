package com.day.line.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class, UserProfile::class, JournalEntry::class],
    version = 6,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun journalDao(): JournalDao
}
