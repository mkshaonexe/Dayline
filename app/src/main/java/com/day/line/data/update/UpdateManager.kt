package com.day.line.data.update

import com.day.line.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateManager @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun checkForUpdate(): AppVersion? {
        return try {
            val versions = supabase.postgrest.from("app_updates")
                .select()
                .decodeList<AppVersion>()
            
            // Find the latest version code
            versions.maxByOrNull { it.versionCode }?.takeIf { 
                it.versionCode > BuildConfig.VERSION_CODE 
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
