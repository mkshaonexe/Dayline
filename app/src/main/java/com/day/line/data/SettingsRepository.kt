package com.day.line.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dayline_settings", Context.MODE_PRIVATE)
    
    private val _isDarkTheme = MutableStateFlow(prefs.getBoolean("is_dark_theme", false))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    private val _themeColor = MutableStateFlow(prefs.getString("theme_color", "Orange") ?: "Orange")
    val themeColor: StateFlow<String> = _themeColor.asStateFlow()

    private val _isFirstRun = MutableStateFlow(prefs.getBoolean("is_first_run", true))
    val isFirstRun: StateFlow<Boolean> = _isFirstRun.asStateFlow()

    private val _userName = MutableStateFlow(prefs.getString("user_name", "") ?: "")
    val userName: StateFlow<String> = _userName.asStateFlow()

    fun setDarkTheme(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_theme", isDark).apply()
        _isDarkTheme.value = isDark
    }
    
    fun setThemeColor(colorName: String) {
        prefs.edit().putString("theme_color", colorName).apply()
        _themeColor.value = colorName
    }

    fun setFirstRunCompleted() {
        prefs.edit().putBoolean("is_first_run", false).apply()
        _isFirstRun.value = false
    }

    fun setUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
        _userName.value = name
    }
}
