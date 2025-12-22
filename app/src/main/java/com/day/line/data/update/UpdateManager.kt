package com.day.line.data.update

import android.util.Log
import com.day.line.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateManager @Inject constructor(
    private val supabase: SupabaseClient,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) {
    companion object {
        private const val TAG = "UpdateManager"
        private const val PREFS_NAME = "update_prefs"
        private const val KEY_FIRST_DETECTED = "first_detected_"
    }
    
    sealed class UpdateState {
        object NoUpdate : UpdateState()
        data class OptionalUpdate(val version: AppVersion) : UpdateState()
        data class ForcedUpdate(val version: AppVersion, val reason: String) : UpdateState()
    }
    
    private val prefs by lazy { 
        context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE) 
    }

    suspend fun checkUpdateState(): UpdateState {
        val latestVersion = checkForUpdate() ?: return UpdateState.NoUpdate
        val currentVersionCode = BuildConfig.VERSION_CODE
        
        // 1. Check if critical in DB
        if (latestVersion.forceUpdate) {
            return UpdateState.ForcedUpdate(latestVersion, "Critical update required.")
        }

        // 2. Check min supported version
        if (currentVersionCode < latestVersion.minSupportedVersion) {
            return UpdateState.ForcedUpdate(latestVersion, "Current version no longer supported.")
        }
        
        // 3. Grace Period Check (3 Days)
        val firstDetectedTime = getFirstDetectedTime(latestVersion.versionCode)
        if (firstDetectedTime == 0L) {
            // First time seeing this update, save timestamp
            saveFirstDetectedTime(latestVersion.versionCode, System.currentTimeMillis())
            // It's technically optional right now (day 0)
            return UpdateState.OptionalUpdate(latestVersion)
        } else {
            val threeDaysMillis = 3 * 24 * 60 * 60 * 1000L
            val elapsed = System.currentTimeMillis() - firstDetectedTime
            if (elapsed > threeDaysMillis) {
                return UpdateState.ForcedUpdate(latestVersion, "Update required for continued use.")
            }
        }

        return UpdateState.OptionalUpdate(latestVersion)
    }

    private suspend fun checkForUpdate(): AppVersion? {
        return try {
            val versions = supabase.postgrest.from("app_updates")
                .select()
                .decodeList<AppVersion>()
            
            // Find the latest version code
            val latestVersion = versions.maxByOrNull { it.versionCode }
            
            if (latestVersion != null && latestVersion.versionCode > BuildConfig.VERSION_CODE) {
                return latestVersion
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun getFirstDetectedTime(versionCode: Int): Long {
        return prefs.getLong(KEY_FIRST_DETECTED + versionCode, 0L)
    }
    
    private fun saveFirstDetectedTime(versionCode: Int, time: Long) {
        prefs.edit().putLong(KEY_FIRST_DETECTED + versionCode, time).apply()
    }
}
