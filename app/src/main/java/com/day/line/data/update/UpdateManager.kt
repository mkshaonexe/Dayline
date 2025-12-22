package com.day.line.data.update

import android.util.Log
import com.day.line.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateManager @Inject constructor(
    private val supabase: SupabaseClient
) {
    companion object {
        private const val TAG = "UpdateManager"
    }
    
    suspend fun checkForUpdate(): AppVersion? {
        return try {
            Log.d(TAG, "Checking for updates...")
            Log.d(TAG, "Current version: ${BuildConfig.VERSION_NAME} (code: ${BuildConfig.VERSION_CODE})")
            
            val versions = supabase.postgrest.from("app_updates")
                .select()
                .decodeList<AppVersion>()
            
            Log.d(TAG, "Fetched ${versions.size} version(s) from Supabase")
            versions.forEach { version ->
                Log.d(TAG, "Found version: ${version.versionName} (code: ${version.versionCode})")
            }
            
            // Find the latest version code
            val latestVersion = versions.maxByOrNull { it.versionCode }
            
            if (latestVersion != null) {
                Log.d(TAG, "Latest version: ${latestVersion.versionName} (code: ${latestVersion.versionCode})")
                
                if (latestVersion.versionCode > BuildConfig.VERSION_CODE) {
                    Log.d(TAG, "Update available! ${latestVersion.versionCode} > ${BuildConfig.VERSION_CODE}")
                    return latestVersion
                } else {
                    Log.d(TAG, "App is up to date. ${latestVersion.versionCode} <= ${BuildConfig.VERSION_CODE}")
                }
            } else {
                Log.w(TAG, "No versions found in database")
            }
            
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking for updates", e)
            e.printStackTrace()
            null
        }
    }
}
