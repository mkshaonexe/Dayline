# Supabase Setup Script for Dayline App

Run this script in the [Supabase SQL Editor](https://supabase.com/dashboard/project/_/sql) to reset and set up your database.

> [!WARNING]
> This script starts by **DROPPING** existing tables (`app_updates`, `profiles`). All data in these tables will be lost.

```sql
-- ==========================================
-- 1. RESET (DROP EXISTING TABLES)
-- ==========================================
DROP TABLE IF EXISTS public.app_updates CASCADE;
DROP TABLE IF EXISTS public.profiles CASCADE;
-- Add other tables here if needed (e.g. public.friendships, public.posts)

-- ==========================================
-- 2. APP UPDATES TABLE
-- ==========================================
-- This table stores version info to force updates and show notifications.
CREATE TABLE public.app_updates (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    version_name text NOT NULL,                    -- e.g. "0.5.7"
    version_code integer NOT NULL,                 -- e.g. 59 (Must match build.gradle)
    changelog text,                                -- The notification message / update description
    download_url text NOT NULL,                    -- Link to Play Store or Direct Download
    is_critical boolean DEFAULT false,             -- If true, blocks app usage immediately
    min_supported_version integer DEFAULT 0,       -- Versions below this are blocked immediately
    release_date timestamp with time zone DEFAULT now(),
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT app_updates_pkey PRIMARY KEY (id)
);

-- Enable RLS (Row Level Security)
ALTER TABLE public.app_updates ENABLE ROW LEVEL SECURITY;

-- Policy: Everyone can read update info (Public)
CREATE POLICY "Public Read Access" 
ON public.app_updates FOR SELECT 
USING (true);

-- Policy: Only Service Role (or Admin) can insert/update (Optional, usually handled via Dashboard)
-- CREATE POLICY "Admin Write Access" ON public.app_updates FOR ALL USING (auth.role() = 'service_role');


-- ==========================================
-- 3. PROFILES TABLE (User Login & Data)
-- ==========================================
-- This table mimics the Local UserProfile but in the cloud, linked to Supabase Auth.
CREATE TABLE public.profiles (
    id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    email text,
    full_name text,
    avatar_url text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT profiles_pkey PRIMARY KEY (id)
);

-- Enable RLS
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;

-- Policy: Public can view profiles (needed for Community/Friend search)
CREATE POLICY "Public Profiles are viewable by everyone" 
ON public.profiles FOR SELECT 
USING (true);

-- Policy: Users can insert their own profile
CREATE POLICY "Users can insert their own profile" 
ON public.profiles FOR INSERT 
WITH CHECK (auth.uid() = id);

-- Policy: Users can update their own profile
CREATE POLICY "Users can update own profile" 
ON public.profiles FOR UPDATE 
USING (auth.uid() = id);

-- ==========================================
-- 4. AUTO-CREATE PROFILE TRIGGER
-- ==========================================
-- Automatically creates a row in public.profiles when a new user signs up via Auth.
CREATE OR REPLACE FUNCTION public.handle_new_user() 
RETURNS trigger AS $$
BEGIN
  INSERT INTO public.profiles (id, email, full_name, avatar_url)
  VALUES (new.id, new.email, new.raw_user_meta_data->>'full_name', new.raw_user_meta_data->>'avatar_url');
  RETURN new;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE PROCEDURE public.handle_new_user();

-- ==========================================
-- CONTEXT: UPDATE LOGIC
-- ==========================================
/*
How to Use app_updates:
1. When you release a new app version (e.g. v0.5.7, code 59):
   INSERT INTO public.app_updates (version_name, version_code, changelog, download_url, is_critical, min_supported_version)
   VALUES ('0.5.7', 59, 'New features included! Fixed crash on login.', 'https://play.google.com/store/apps/details?id=com.day.line', false, 50);

2. If you want to FORCE everyone to update:
   UPDATE public.app_updates SET is_critical = true WHERE version_code = 59;
   
   OR set min_supported_version to the current version in a new row.
*/
```
