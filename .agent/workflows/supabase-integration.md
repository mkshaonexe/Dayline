---
description: How to add Supabase for app update checking with .env configuration
---

# Supabase Integration Guide for App Update Checking

This guide walks you through integrating Supabase into your Android project to check for app updates, including secure credential management using environment variables.

## Prerequisites

- A Supabase account (sign up at https://supabase.com)
- Android Studio
- Your Dayline project

## Step 1: Create Supabase Project

1. Go to https://supabase.com and sign in
2. Click "New Project"
3. Fill in the details:
   - **Project Name**: `dayline-updates` (or your preferred name)
   - **Database Password**: Create a strong password (save this!)
   - **Region**: Choose closest to your users
4. Wait for the project to be created (takes ~2 minutes)

## Step 2: Get Supabase Credentials

After your project is created:

1. Go to **Project Settings** (gear icon in sidebar)
2. Navigate to **API** section
3. Copy these values (you'll need them later):
   - **Project URL**: `https://xxxxxxxxxxxxx.supabase.co`
   - **anon/public key**: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...` (long string)

## Step 3: Create Database Table for App Updates

1. In Supabase Dashboard, go to **SQL Editor**
2. Click **New Query**
3. Paste and run this SQL:

```sql
-- Create app_updates table
CREATE TABLE app_updates (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    version_name TEXT NOT NULL,
    version_code INTEGER NOT NULL,
    release_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    download_url TEXT NOT NULL,
    changelog TEXT,
    is_critical BOOLEAN DEFAULT FALSE,
    min_supported_version INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create index for faster queries
CREATE INDEX idx_version_code ON app_updates(version_code DESC);

-- Enable Row Level Security (RLS)
ALTER TABLE app_updates ENABLE ROW LEVEL SECURITY;

-- Create policy to allow public read access (for checking updates)
CREATE POLICY "Allow public read access" ON app_updates
    FOR SELECT
    USING (true);

-- Create policy to allow authenticated insert/update (for you to manage updates)
CREATE POLICY "Allow authenticated insert" ON app_updates
    FOR INSERT
    WITH CHECK (auth.role() = 'authenticated');

CREATE POLICY "Allow authenticated update" ON app_updates
    FOR UPDATE
    USING (auth.role() = 'authenticated');

-- Insert initial version (example)
INSERT INTO app_updates (version_name, version_code, download_url, changelog, is_critical, min_supported_version)
VALUES (
    '0.5.6',
    56,
    'https://play.google.com/store/apps/details?id=com.day.line',
    'Initial release with Supabase integration',
    false,
    50
);
```

## Step 4: Add Supabase Dependencies

### 4.1 Update `libs.versions.toml`

Add these versions to the `[versions]` section:

```toml
supabase = "3.0.2"
ktor = "3.0.2"
```

Add these libraries to the `[libraries]` section:

```toml
supabase-postgrest = { module = "io.github.jan-tennert.supabase:postgrest-kt", version.ref = "supabase" }
supabase-realtime = { module = "io.github.jan-tennert.supabase:realtime-kt", version.ref = "supabase" }
ktor-client-android = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
```

### 4.2 Update `app/build.gradle.kts`

Add to the `dependencies` block:

```kotlin
// Supabase
implementation(libs.supabase.postgrest)
implementation(libs.supabase.realtime)
implementation(libs.ktor.client.android)
implementation(libs.ktor.client.core)
```

## Step 5: Setup Environment Variables (.env)

### 5.1 Create `.env` file

In your project root (`e:\Cursor Play ground\Dayline\`), create a file named `.env`:

```env
# Supabase Configuration
SUPABASE_URL=https://xxxxxxxxxxxxx.supabase.co
SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# Replace the values above with your actual Supabase credentials from Step 2
```

### 5.2 Add `.env` to `.gitignore`

**CRITICAL**: Add this to your `.gitignore` file to prevent committing secrets:

```gitignore
# Environment variables
.env
local.properties
```

### 5.3 Create `.env.example` (Template)

Create a template file `.env.example` for other developers:

```env
# Supabase Configuration
SUPABASE_URL=your_supabase_project_url_here
SUPABASE_ANON_KEY=your_supabase_anon_key_here
```

This file CAN be committed to git as it doesn't contain real credentials.

## Step 6: Load Environment Variables in Android

### 6.1 Add dotenv plugin to `build.gradle.kts` (project level)

```kotlin
plugins {
    // ... existing plugins
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}
```

### 6.2 Update `app/build.gradle.kts`

Add at the top of the file:

```kotlin
import java.util.Properties
import java.io.FileInputStream

// Load .env file
val envFile = rootProject.file(".env")
val envProperties = Properties()
if (envFile.exists()) {
    envProperties.load(FileInputStream(envFile))
}
```

Then in the `android` block, add `buildConfigField`:

```kotlin
android {
    // ... existing config
    
    buildFeatures {
        buildConfig = true
        compose = true
    }
    
    defaultConfig {
        // ... existing config
        
        // Add Supabase credentials from .env
        buildConfigField("String", "SUPABASE_URL", "\"${envProperties.getProperty("SUPABASE_URL", "")}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${envProperties.getProperty("SUPABASE_ANON_KEY", "")}\"")
    }
}
```

## Step 7: Create Supabase Client

Create `app/src/main/java/com/day/line/data/supabase/SupabaseClient.kt`:

```kotlin
package com.day.line.data.supabase

import com.day.line.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Realtime)
    }
}
```

## Step 8: Create Update Check Models

Create `app/src/main/java/com/day/line/data/models/AppUpdate.kt`:

```kotlin
package com.day.line.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppUpdate(
    val id: String,
    @SerialName("version_name")
    val versionName: String,
    @SerialName("version_code")
    val versionCode: Int,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("download_url")
    val downloadUrl: String,
    val changelog: String? = null,
    @SerialName("is_critical")
    val isCritical: Boolean = false,
    @SerialName("min_supported_version")
    val minSupportedVersion: Int
)
```

## Step 9: Create Update Repository

Create `app/src/main/java/com/day/line/data/repository/UpdateRepository.kt`:

```kotlin
package com.day.line.data.repository

import com.day.line.BuildConfig
import com.day.line.data.models.AppUpdate
import com.day.line.data.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class UpdateRepository {
    
    suspend fun checkForUpdates(): Result<AppUpdate?> {
        return try {
            val currentVersionCode = BuildConfig.VERSION_CODE
            
            // Get the latest version from Supabase
            val updates = SupabaseClient.client
                .from("app_updates")
                .select(Columns.ALL) {
                    order("version_code", ascending = false)
                    limit(1)
                }
                .decodeList<AppUpdate>()
            
            val latestUpdate = updates.firstOrNull()
            
            // Check if update is available
            if (latestUpdate != null && latestUpdate.versionCode > currentVersionCode) {
                Result.success(latestUpdate)
            } else {
                Result.success(null) // No update available
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun isVersionSupported(): Result<Boolean> {
        return try {
            val currentVersionCode = BuildConfig.VERSION_CODE
            
            val updates = SupabaseClient.client
                .from("app_updates")
                .select(Columns.ALL) {
                    order("version_code", ascending = false)
                    limit(1)
                }
                .decodeList<AppUpdate>()
            
            val latestUpdate = updates.firstOrNull()
            
            if (latestUpdate != null) {
                Result.success(currentVersionCode >= latestUpdate.minSupportedVersion)
            } else {
                Result.success(true) // No data, assume supported
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## Step 10: Implement Update Check UI

Create a composable to show update dialog:

```kotlin
@Composable
fun UpdateCheckDialog(
    update: AppUpdate,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Available") },
        text = {
            Column {
                Text("Version ${update.versionName} is now available!")
                if (update.changelog != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("What's New:", fontWeight = FontWeight.Bold)
                    Text(update.changelog)
                }
            }
        },
        confirmButton = {
            Button(onClick = onUpdate) {
                Text("Update Now")
            }
        },
        dismissButton = if (!update.isCritical) {
            {
                TextButton(onClick = onDismiss) {
                    Text("Later")
                }
            }
        } else null
    )
}
```

## Step 11: Check for Updates on App Start

In your `MainActivity.kt` or main screen:

```kotlin
val updateRepository = remember { UpdateRepository() }
var updateAvailable by remember { mutableStateOf<AppUpdate?>(null) }
var showUpdateDialog by remember { mutableStateOf(false) }

LaunchedEffect(Unit) {
    // Check for updates
    updateRepository.checkForUpdates().onSuccess { update ->
        if (update != null) {
            updateAvailable = update
            showUpdateDialog = true
        }
    }
    
    // Check if version is supported
    updateRepository.isVersionSupported().onSuccess { isSupported ->
        if (!isSupported) {
            // Force update - show non-dismissible dialog
        }
    }
}

if (showUpdateDialog && updateAvailable != null) {
    UpdateCheckDialog(
        update = updateAvailable!!,
        onDismiss = { showUpdateDialog = false },
        onUpdate = {
            // Open Play Store or download URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateAvailable!!.downloadUrl))
            context.startActivity(intent)
        }
    )
}
```

## Step 12: Sync and Build

// turbo
1. Sync Gradle files
2. Build the project to ensure everything works

## Managing Updates

To add a new version to Supabase:

1. Go to Supabase Dashboard → Table Editor → `app_updates`
2. Click "Insert row"
3. Fill in:
   - `version_name`: "0.5.7"
   - `version_code`: 57
   - `download_url`: Your Play Store or APK URL
   - `changelog`: "Bug fixes and improvements"
   - `is_critical`: false (or true for forced updates)
   - `min_supported_version`: 50 (minimum version code that's still supported)

## Security Best Practices

1. ✅ **Never commit `.env`** - Always in `.gitignore`
2. ✅ **Use RLS policies** - Supabase Row Level Security protects your data
3. ✅ **Use anon key for client** - Never use service_role key in the app
4. ✅ **Validate on server** - For critical operations, add server-side validation
5. ✅ **Rotate keys if exposed** - If you accidentally commit keys, rotate them in Supabase

## Troubleshooting

**Build fails with "SUPABASE_URL not found":**
- Ensure `.env` file exists in project root
- Check that values don't have quotes in `.env`
- Rebuild project (Clean → Rebuild)

**Can't connect to Supabase:**
- Verify URL and key are correct
- Check internet permission in `AndroidManifest.xml`
- Ensure RLS policies are set correctly

**Updates not showing:**
- Check version_code in `app/build.gradle.kts`
- Verify data exists in Supabase table
- Check Logcat for errors

## Summary

You now have:
- ✅ Supabase integrated for update checking
- ✅ Secure credential management with `.env`
- ✅ Database table for managing app versions
- ✅ Repository pattern for clean architecture
- ✅ UI components for showing updates
- ✅ Support for critical/forced updates
