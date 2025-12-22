-- ==========================================
-- DAYLINE APP - SUPABASE SETUP SCRIPT
-- ==========================================
-- Copy and paste this entire file into Supabase SQL Editor
-- This will reset and recreate all necessary tables

-- ==========================================
-- 1. DROP EXISTING TABLES (RESET)
-- ==========================================
DROP TABLE IF EXISTS public.app_updates CASCADE;
DROP TABLE IF EXISTS public.profiles CASCADE;

-- ==========================================
-- 2. CREATE APP_UPDATES TABLE
-- ==========================================
CREATE TABLE public.app_updates (
  id uuid NOT NULL DEFAULT gen_random_uuid(),
  version_name text NOT NULL,
  version_code integer NOT NULL,
  release_date timestamp with time zone DEFAULT now(),
  download_url text NOT NULL,
  changelog text,
  is_critical boolean DEFAULT false,
  min_supported_version integer NOT NULL DEFAULT 0,
  created_at timestamp with time zone DEFAULT now(),
  updated_at timestamp with time zone DEFAULT now(),
  CONSTRAINT app_updates_pkey PRIMARY KEY (id)
);

-- Enable Row Level Security
ALTER TABLE public.app_updates ENABLE ROW LEVEL SECURITY;

-- Allow everyone to read updates (public access)
CREATE POLICY "Public Read Access" 
ON public.app_updates FOR SELECT 
USING (true);

-- ==========================================
-- 3. CREATE PROFILES TABLE
-- ==========================================
CREATE TABLE public.profiles (
    id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    email text,
    full_name text,
    avatar_url text,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now(),
    CONSTRAINT profiles_pkey PRIMARY KEY (id)
);

-- Enable Row Level Security
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;

-- Public can view profiles
CREATE POLICY "Public Profiles are viewable by everyone" 
ON public.profiles FOR SELECT 
USING (true);

-- Users can insert their own profile
CREATE POLICY "Users can insert their own profile" 
ON public.profiles FOR INSERT 
WITH CHECK (auth.uid() = id);

-- Users can update their own profile
CREATE POLICY "Users can update own profile" 
ON public.profiles FOR UPDATE 
USING (auth.uid() = id);

-- ==========================================
-- 4. AUTO-CREATE PROFILE TRIGGER
-- ==========================================
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
-- 5. INSERT SAMPLE UPDATE (OPTIONAL)
-- ==========================================
-- Uncomment and modify this to add your first update
-- INSERT INTO public.app_updates (version_name, version_code, changelog, download_url, is_critical, min_supported_version)
-- VALUES ('0.5.7', 57, 'New features and bug fixes!', 'https://play.google.com/store/apps/details?id=com.day.line', false, 50);
