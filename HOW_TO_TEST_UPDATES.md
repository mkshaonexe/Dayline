# How to Test the Update System

## Current Situation:
- ✅ Your app is now **Version 0.6.9** (versionCode: 69)
- ✅ Build successful and pushed to GitHub

## Step-by-Step Guide:

### Step 1: Setup Supabase Database
1. Go to your Supabase Dashboard: https://supabase.com/dashboard
2. Click on your project
3. Go to **SQL Editor** (left sidebar)
4. Copy the entire content from `supabase_setup.sql` file
5. Paste it into the SQL Editor
6. Click **Run** (or press Ctrl+Enter)
7. You should see: "Success. No rows returned"

### Step 2: Add Version 0.7.0 to Trigger Update
1. Stay in the **SQL Editor**
2. Copy and paste this SQL:

```sql
INSERT INTO public.app_updates (
    version_name, 
    version_code, 
    changelog, 
    download_url, 
    is_critical, 
    min_supported_version
)
VALUES (
    '0.7.0',                                                    -- New version name
    70,                                                         -- New version code (must be > 69)
    'New features and improvements! Update now to continue.',  -- This shows in notification
    'https://t.me/appdayline',                                 -- Download link (Telegram channel)
    false,                                                      -- Not critical (gives 3 days grace)
    60                                                          -- Minimum supported version
);
```

3. Click **Run**
4. You should see: "Success. 1 row(s) affected"

### Step 3: Test the App
1. **Install the new 0.6.9 APK** on your phone (from `app/build/outputs/apk/debug/`)
2. **Open the app** - it will check for updates in the background
3. You should see a **notification** saying:
   - Title: "Update Available: 0.7.0"
   - Message: "New features and improvements! Update now to continue."
4. **Click the notification** or go to **Settings** → **Version 0.6.9**
5. Click the refresh icon next to the version
6. A dialog will appear with:
   - **Download Update** button (opens Telegram)
   - **Telegram** button (opens channel)
   - **Later** button (dismiss)

### Step 4: Test Force Update (After 3 Days)
If you want to test the "force update" screen immediately:

1. Go back to Supabase SQL Editor
2. Run this SQL:

```sql
UPDATE public.app_updates 
SET is_critical = true 
WHERE version_code = 70;
```

3. **Close and reopen the app**
4. You should see a **full-screen blocking page** that says:
   - "Dayline app stop working ask for the update"
   - "Update Now" button
   - "Contact Support" button

### What Each Field Does:
- **version_name**: What users see (e.g. "0.7.0")
- **version_code**: Must be a number HIGHER than current (69 → 70)
- **changelog**: The message shown in notification and dialog
- **download_url**: Where "Download Update" button goes (Telegram/Play Store)
- **is_critical**: 
  - `false` = Users get 3 days to update
  - `true` = App blocks immediately
- **min_supported_version**: Any version below this number gets blocked immediately

### Quick Reference SQL Commands:

**Check what's in the database:**
```sql
SELECT * FROM public.app_updates ORDER BY version_code DESC;
```

**Delete all updates (start fresh):**
```sql
DELETE FROM public.app_updates;
```

**Add a new update:**
```sql
INSERT INTO public.app_updates (version_name, version_code, changelog, download_url, is_critical, min_supported_version)
VALUES ('0.7.1', 71, 'Bug fixes', 'https://t.me/appdayline', false, 60);
```
