-- schema.sql
-- Database schema for Babua DSA Patterns Sheet 2025

-- Note: We assume you are using Supabase Auth, which maintains its own auth.users table.
-- We create our own public.users table that can optionally link to auth.users

CREATE TABLE public.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.patterns (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE public.problems (
    id SERIAL PRIMARY KEY,
    pattern_id INTEGER REFERENCES public.patterns(id) ON DELETE CASCADE,
    number INTEGER, -- LeetCode or arbitrary number
    title VARCHAR(255) NOT NULL,
    difficulty VARCHAR(50) CHECK (difficulty IN ('Easy', 'Medium', 'Hard')),
    leetcode_url VARCHAR(255),
    video_url VARCHAR(255),
    sort_order INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE public.user_progress (
    id SERIAL PRIMARY KEY,
    user_id UUID REFERENCES public.users(id) ON DELETE CASCADE,
    problem_id INTEGER REFERENCES public.problems(id) ON DELETE CASCADE,
    completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP WITH TIME ZONE,
    UNIQUE(user_id, problem_id)
);

-- Indexes for performance
CREATE INDEX idx_user_progress_user_id ON public.user_progress(user_id);
CREATE INDEX idx_problems_pattern_id ON public.problems(pattern_id);
