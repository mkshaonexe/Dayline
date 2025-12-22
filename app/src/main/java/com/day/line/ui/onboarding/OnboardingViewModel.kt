package com.day.line.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.line.data.SettingsRepository
import com.day.line.data.UserProfile
import com.day.line.data.UserProfileDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userProfileDao: UserProfileDao
) : ViewModel() {

    fun completeOnboarding(userName: String) {
        viewModelScope.launch {
            // Save to Preferences
            settingsRepository.setUserName(userName)
            settingsRepository.setFirstRunCompleted()

            // Update User Profile in Database
            // Assuming ID 1 is the default user
            val currentProfile = userProfileDao.getUserProfileSync()
            val newProfile = currentProfile?.copy(name = userName) ?: UserProfile(name = userName)
            userProfileDao.insertOrUpdateProfile(newProfile)
        }
    }
}
