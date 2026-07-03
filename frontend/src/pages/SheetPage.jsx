import React, { useState, useEffect } from 'react';
import { PatternAccordion } from '../components/PatternAccordion';
import { ProgressCard } from '../components/ProgressCard';

const API_URL = import.meta.env.DEV ? 'http://localhost:8080/api' : 'https://cto-bhaiya.onrender.com/api';

export const SheetPage = ({ onUpdateStreak }) => {
  const [session, setSession] = useState(null);
  const [patterns, setPatterns] = useState([]);
  const [problems, setProblems] = useState([]);
  const [userProgress, setUserProgress] = useState({});
  const [userBookmarks, setUserBookmarks] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchSheetData();
  }, []);

  useEffect(() => {
    fetchProgress();
    fetchBookmarks();
  }, []);

  const fetchSheetData = async () => {
    try {
      // In a real app, you'd fetch from your Spring Boot backend. 
      // Using mock data here for the UI if backend is offline.
      const patternsRes = await fetch(`${API_URL}/sheet/patterns`).catch(() => null);
      const problemsRes = await fetch(`${API_URL}/sheet/problems`).catch(() => null);
      
      if (patternsRes && patternsRes.ok && problemsRes && problemsRes.ok) {
        setPatterns(await patternsRes.json());
        setProblems(await problemsRes.json());
      } else {
        // Fallback to sample data if backend isn't running yet
        setPatterns([
          { id: 1, name: 'Two Pointers' },
          { id: 2, name: 'Sliding Window' }
        ]);
        setProblems([
          { id: 1, pattern: {id: 1}, number: 88, title: 'Merge Sorted Array', difficulty: 'Easy', leetcodeUrl: '#' },
          { id: 2, pattern: {id: 1}, number: 15, title: '3Sum', difficulty: 'Medium', leetcodeUrl: '#' },
          { id: 3, pattern: {id: 2}, number: 3, title: 'Longest Substring', difficulty: 'Medium', leetcodeUrl: '#' },
        ]);
      }
    } catch (error) {
      console.error('Error fetching sheet data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchProgress = async () => {
      const token = localStorage.getItem('dsa_token');
      if (!token) return;

      try {
        const res = await fetch(`${API_URL}/progress`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (res.ok) {
          const data = await res.json();
          setUserProgress(data);
        } else if (res.status === 401) {
           // Token might be expired
           localStorage.removeItem('dsa_token');
           localStorage.removeItem('dsa_email');
        }
      } catch (err) {
        console.error("Error fetching progress:", err);
      }
  };

  const fetchBookmarks = async () => {
      const token = localStorage.getItem('dsa_token');
      if (!token) return;

      try {
        const res = await fetch(`${API_URL}/progress/bookmarks`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (res.ok) {
          const data = await res.json();
          setUserBookmarks(data);
        }
      } catch (err) {
        console.error("Error fetching bookmarks:", err);
      }
  };

  const toggleProblem = async (problemId) => {
    const token = localStorage.getItem('dsa_token');
    if (!token) {
      alert("Please sign in to track progress!");
      return;
    }

    const newStatus = !userProgress[problemId];
    
    // Optimistic update
    setUserProgress(prev => ({...prev, [problemId]: newStatus}));

    try {
      const res = await fetch(`${API_URL}/progress/${problemId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ completed: newStatus })
      });
      if (res.ok) {
        const data = await res.json();
        if (data.streak !== undefined && onUpdateStreak) {
          localStorage.setItem('dsa_streak', data.streak);
          onUpdateStreak(data.streak);
        }
      }
    } catch (err) {
      // Revert on error
      setUserProgress(prev => ({...prev, [problemId]: !newStatus}));
      console.error("Failed to update progress:", err);
    }
  };

  const toggleBookmark = async (problemId) => {
    const token = localStorage.getItem('dsa_token');
    if (!token) {
      alert("Please sign in to bookmark questions!");
      return;
    }

    const newStatus = !userBookmarks[problemId];
    
    // Optimistic update
    setUserBookmarks(prev => ({...prev, [problemId]: newStatus}));

    try {
      const res = await fetch(`${API_URL}/progress/${problemId}/bookmark`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ bookmarked: newStatus })
      });
      if (!res.ok) {
        throw new Error("Failed to update bookmark");
      }
    } catch (err) {
      // Revert on error
      setUserBookmarks(prev => ({...prev, [problemId]: !newStatus}));
      console.error("Failed to update bookmark:", err);
    }
  };

  const completedCount = Object.values(userProgress).filter(Boolean).length;
  const totalCount = problems.length;

  if (loading) return <div className="text-center py-20">Loading sheet...</div>;

  return (
    <div>
      <div className="flex flex-col md:flex-row justify-between items-start gap-8 mb-12">
        <div>
          <div className="text-sm font-semibold text-emerald-500 uppercase tracking-widest mb-2 flex items-center gap-2">
            <span>Main Sheet</span>
            <span className="text-gray-600">•</span>
            <span className="text-gray-400">by CTO Bhaiya</span>
          </div>
          <h1 className="text-4xl md:text-5xl font-bold tracking-tight mb-4 flex items-center gap-3">
            <span>🚀</span> Babua DSA Patterns Sheet 2025
          </h1>
          <p className="text-gray-400 text-lg max-w-2xl">
            The core 30-pattern sheet — the heart of the 90 Day Challenge.
          </p>
        </div>
        <ProgressCard completedCount={completedCount} totalCount={totalCount} />
      </div>

      <div className="space-y-6">
        {patterns.map(pattern => {
          const patternProblems = problems.filter(p => p.pattern && p.pattern.id === pattern.id || p.pattern_id === pattern.id);
          return (
            <PatternAccordion 
              key={pattern.id}
              pattern={pattern}
              problems={patternProblems}
              userProgress={userProgress}
              userBookmarks={userBookmarks}
              onToggleProblem={toggleProblem}
              onToggleBookmark={toggleBookmark}
            />
          )
        })}
      </div>
    </div>
  );
};
