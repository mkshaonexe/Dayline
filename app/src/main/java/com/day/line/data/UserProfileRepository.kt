package com.day.line.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()
    
    suspend fun updateUserName(name: String) {
        val currentProfile = UserProfile(name = name)
        userProfileDao.insertOrUpdateProfile(currentProfile)
    }
    
    suspend fun updateProfileImage(imageUri: String?) {
        val currentProfile = UserProfile(profileImageUri = imageUri)
        userProfileDao.insertOrUpdateProfile(currentProfile)
    }
    
    suspend fun updateProfile(name: String, imageUri: String?) {
        val profile = UserProfile(name = name, profileImageUri = imageUri)
        userProfileDao.insertOrUpdateProfile(profile)
    }
}
