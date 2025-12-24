package com.day.line.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) {
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()
    
    suspend fun updateUserName(name: String) {
        val currentProfile = userProfileDao.getUserProfileSync() // Need simple get or handle null
        val updatedProfile = currentProfile?.copy(name = name) ?: UserProfile(name = name)
        userProfileDao.insertOrUpdateProfile(updatedProfile)
    }
    
    suspend fun updateProfileImage(imageUri: String?) {
        val internalUri = saveImageToInternalStorage(imageUri)
        val currentProfile = userProfileDao.getUserProfileSync()
        val updatedProfile = currentProfile?.copy(profileImageUri = internalUri) 
            ?: UserProfile(profileImageUri = internalUri)
        userProfileDao.insertOrUpdateProfile(updatedProfile)
    }
    
    suspend fun updateProfile(name: String, imageUri: String?) {
        // If the uri is unchanged, don't re-copy
        val currentProfile = userProfileDao.getUserProfileSync()
        
        val finalUri = if (imageUri != currentProfile?.profileImageUri) {
            saveImageToInternalStorage(imageUri)
        } else {
            imageUri
        }

        val updatedProfile = currentProfile?.copy(name = name, profileImageUri = finalUri)
            ?: UserProfile(name = name, profileImageUri = finalUri)
            
        userProfileDao.insertOrUpdateProfile(updatedProfile)
    }

    suspend fun updateWakeUpTime(time: String) {
        val currentProfile = userProfileDao.getUserProfileSync()
        val updatedProfile = currentProfile?.copy(wakeUpTime = time)
            ?: UserProfile(wakeUpTime = time)
        userProfileDao.insertOrUpdateProfile(updatedProfile)
    }

    private fun saveImageToInternalStorage(uriString: String?): String? {
        if (uriString == null) return null
        
        // If it's already a local file path, return it
        if (!uriString.startsWith("content://")) return uriString

        return try {
            val uri = android.net.Uri.parse(uriString)
            val inputStream = context.contentResolver.openInputStream(uri)
            
            if (inputStream != null) {
                // Create a file in the app's private storage
                val directory = java.io.File(context.filesDir, "profile_images")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                
                val fileName = "profile_${System.currentTimeMillis()}.jpg"
                val file = java.io.File(directory, fileName)
                
                val outputStream = java.io.FileOutputStream(file)
                inputStream.copyTo(outputStream)
                
                inputStream.close()
                outputStream.close()
                
                file.absolutePath
            } else {
                uriString
            }
        } catch (e: Exception) {
            e.printStackTrace()
            uriString
        }
    }
}
