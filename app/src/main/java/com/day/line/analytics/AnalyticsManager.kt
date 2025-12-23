package com.day.line.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor() {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logEvent(eventName: String, params: Bundle = Bundle()) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    // Task Events
    fun logTaskCreated(hasTime: Boolean, hasIcon: Boolean) {
        val bundle = Bundle().apply {
            putBoolean("has_time", hasTime)
            putBoolean("has_icon", hasIcon)
        }
        logEvent("task_created", bundle)
    }

    fun logTaskCompleted(isUserTask: Boolean) {
        val bundle = Bundle().apply {
            putString("task_type", if (isUserTask) "user_task" else "fixed_task")
        }
        logEvent("task_completed", bundle)
    }
    
    fun logTaskUncompleted(isUserTask: Boolean) {
        val bundle = Bundle().apply {
             putString("task_type", if (isUserTask) "user_task" else "fixed_task")
        }
        logEvent("task_uncompleted", bundle)
    }

    fun logTaskDeleted() {
        logEvent("task_deleted")
    }
    
    fun logSubtaskToggled(isCompleted: Boolean) {
        val bundle = Bundle().apply {
            putBoolean("is_completed", isCompleted)
        }
        logEvent("subtask_toggled", bundle)
    }

    // Onboarding Events
    fun logOnboardingCompleted(userNameLength: Int) {
        val bundle = Bundle().apply {
            putInt("name_length", userNameLength)
        }
        logEvent("onboarding_completed", bundle)
    }

    // Settings Events
    fun logThemeChanged(isDark: Boolean) {
        val bundle = Bundle().apply {
            putString("theme_mode", if (isDark) "dark" else "light")
        }
        logEvent("theme_changed", bundle)
    }

    fun logColorChanged(colorName: String) {
        val bundle = Bundle().apply {
            putString("theme_color", colorName)
        }
        logEvent("theme_color_changed", bundle)
    }

    // New Interaction Events
    fun logTutorialClicked() {
        logEvent("tutorial_clicked")
    }

    fun logEarlyAccessClicked() {
        logEvent("early_access_clicked")
    }

    fun logCommunityLinkOpen(platform: String) {
        val bundle = Bundle().apply {
            putString("platform", platform)
        }
        logEvent("community_link_opened", bundle)
    }

    fun logFeedbackOpened() {
        logEvent("feedback_opened")
    }

    fun logEditProfileClicked() {
        logEvent("edit_profile_clicked")
    }
    
    fun logActivityGraphViewed() {
        logEvent("activity_graph_viewed")
    }
}
