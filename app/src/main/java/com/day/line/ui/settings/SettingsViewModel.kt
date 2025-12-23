package com.day.line.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.line.data.SettingsRepository
import com.day.line.data.update.AppVersion
import com.day.line.data.update.UpdateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UpdateStatus {
    object Idle : UpdateStatus()
    object Checking : UpdateStatus()
    data class Available(val version: AppVersion) : UpdateStatus()
    object UpToDate : UpdateStatus()
    object Error : UpdateStatus()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val updateManager: UpdateManager,
    private val analyticsManager: com.day.line.analytics.AnalyticsManager
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = settingsRepository.isDarkTheme
    val themeColor: StateFlow<String> = settingsRepository.themeColor
    val hasClickedEarlyAccess: StateFlow<Boolean> = settingsRepository.hasClickedEarlyAccess

    private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val updateStatus: StateFlow<UpdateStatus> = _updateStatus.asStateFlow()

    // Permission states (simplified management via UI events usually, but holding state here is fine)
    // For this implementation, we'll let the UI check permission state via Accompanist or simple Context checks,
    // but the ViewModel can handle the intent to update or check permissions if needed.

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkTheme(isDark)
            analyticsManager.logThemeChanged(isDark)
        }
    }
    
    fun setThemeColor(colorName: String) {
        viewModelScope.launch {
            settingsRepository.setThemeColor(colorName)
            analyticsManager.logColorChanged(colorName)
        }
    }

    fun markEarlyAccessClicked() {
        viewModelScope.launch {
            settingsRepository.setEarlyAccessClicked()
        }
    }

    fun checkForUpdate() {
        viewModelScope.launch {
            _updateStatus.value = UpdateStatus.Checking
            try {
                val state = updateManager.checkUpdateState()
                
                when (state) {
                    is UpdateManager.UpdateState.ForcedUpdate -> {
                        _updateStatus.value = UpdateStatus.Available(state.version)
                    }
                    is UpdateManager.UpdateState.OptionalUpdate -> {
                        _updateStatus.value = UpdateStatus.Available(state.version)
                    }
                    is UpdateManager.UpdateState.NoUpdate -> {
                        _updateStatus.value = UpdateStatus.UpToDate
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _updateStatus.value = UpdateStatus.Error
            }
        }
    }

    fun clearUpdateStatus() {
        _updateStatus.value = UpdateStatus.Idle
    }
}
