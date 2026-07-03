import React, { useState, useEffect } from 'react';

const API_URL = import.meta.env.DEV ? 'http://localhost:8080/api' : 'https://cto-bhaiya.onrender.com/api';

export function LeaderboardPage() {
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLeaderboard = async () => {
      try {
        const response = await fetch(`${API_URL}/stats/leaderboard`);
        if (!response.ok) {
          throw new Error('Failed to fetch leaderboard');
        }
        const data = await response.json();
        setLeaderboard(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchLeaderboard();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-emerald-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-500/10 border border-red-500/20 text-red-400 p-4 rounded-lg text-center">
        Error loading leaderboard: {error}
      </div>
    );
  }

  return (
    <div className="py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2">Leaderboard 🏆</h1>
        <p className="text-gray-400">See who has completed the most patterns in the 90 Day Challenge.</p>
      </div>

      <div className="bg-[#11181a] border border-gray-800 rounded-xl overflow-hidden shadow-lg shadow-black/50">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm text-gray-300">
            <thead className="bg-[#1a2327] text-xs uppercase text-gray-400 font-semibold border-b border-gray-800">
              <tr>
                <th className="px-6 py-4 rounded-tl-xl">Rank</th>
                <th className="px-6 py-4">Hacker</th>
                <th className="px-6 py-4 rounded-tr-xl text-right">Problems Solved</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-800/50">
              {leaderboard.length === 0 ? (
                <tr>
                  <td colSpan="3" className="px-6 py-8 text-center text-gray-500">
                    No one has completed any problems yet. Be the first!
                  </td>
                </tr>
              ) : (
                leaderboard.map((entry, index) => (
                  <tr key={index} className="hover:bg-[#1a2327]/50 transition-colors">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center justify-center w-8 h-8 rounded-full bg-gray-800/50 font-medium text-gray-400">
                        {index + 1 === 1 ? '🥇' : index + 1 === 2 ? '🥈' : index + 1 === 3 ? '🥉' : index + 1}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="font-medium text-white text-base">
                        {entry.name 
                          ? (entry.name.length > 5 ? entry.name.slice(0, 5) + '***' : entry.name) 
                          : 'Anon***'}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <span className="inline-flex items-center justify-center bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 rounded-full px-3 py-1 font-bold text-base">
                        {entry.questionsCompleted}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
