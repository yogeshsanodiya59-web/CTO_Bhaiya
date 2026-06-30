import React from 'react';

export const ProgressCard = ({ completedCount, totalCount }) => {
  const percentage = totalCount === 0 ? 0 : Math.round((completedCount / totalCount) * 100);
  
  return (
    <div className="bg-[#11181a] border border-gray-800 rounded-2xl p-6 w-full max-w-sm shadow-xl">
      <div className="flex justify-between items-end mb-4">
        <div>
          <h2 className="text-xs font-semibold text-gray-500 uppercase tracking-widest mb-1">Your Progress</h2>
          <div className="flex items-baseline gap-2">
            <span className="text-4xl font-bold text-white">{completedCount}</span>
            <span className="text-gray-500 font-medium">/ {totalCount}</span>
          </div>
        </div>
        <div className="text-emerald-500 font-bold text-xl">
          {percentage}%
        </div>
      </div>
      
      <div className="w-full bg-gray-800 rounded-full h-2.5 overflow-hidden">
        <div 
          className="bg-emerald-500 h-2.5 rounded-full transition-all duration-500 ease-out"
          style={{ width: `${percentage}%` }}
        ></div>
      </div>
    </div>
  );
};
