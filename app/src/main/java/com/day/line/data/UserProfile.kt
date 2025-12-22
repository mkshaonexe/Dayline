package com.day.line.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1, // Single user profile
    val name: String = "John Doe",
    val profileImageUri: String? = null
)
