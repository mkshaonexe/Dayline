package com.day.line.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val journalDao: JournalDao
) {
    val allEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()

    suspend fun getEntryCount(): Int {
        return journalDao.getCount()
    }

    suspend fun addEntry(entry: JournalEntry) {
        journalDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: JournalEntry) {
        journalDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: JournalEntry) {
        journalDao.deleteEntry(entry)
    }
}
