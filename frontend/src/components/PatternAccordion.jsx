import React, { useState } from 'react';
import { ChevronDown, ChevronUp, CheckCircle, Circle, PlaySquare, ExternalLink } from 'lucide-react';

const ProblemRow = ({ problem, isCompleted, onToggle }) => {
  return (
    <div className="flex items-center justify-between py-3 px-4 border-b border-gray-800 hover:bg-gray-800/50 transition-colors">
      <div className="flex items-center gap-4 flex-1">
        <button 
          onClick={() => onToggle(problem.id)}
          className="text-emerald-500 hover:text-emerald-400 focus:outline-none transition-colors"
        >
          {isCompleted ? <CheckCircle size={20} /> : <Circle size={20} className="text-gray-500" />}
        </button>
        <span className="text-gray-400 w-8 text-sm">{String(problem.number).padStart(2, '0')}</span>
        <span className={`font-medium ${isCompleted ? 'text-gray-400 line-through' : 'text-gray-200'}`}>
          {problem.title}
        </span>
      </div>
      
      <div className="flex items-center gap-6">
        <span className={`text-sm font-medium w-16 text-center
          ${problem.difficulty === 'Easy' ? 'text-emerald-500' : 
            problem.difficulty === 'Medium' ? 'text-amber-500' : 'text-red-500'}`}>
          {problem.difficulty}
        </span>
        
        <div className="flex items-center gap-3 w-16 justify-end">
          {problem.videoUrl ? (
             <a href={problem.videoUrl} target="_blank" rel="noreferrer" className="text-gray-500 hover:text-blue-400 transition-colors">
               <PlaySquare size={18} />
             </a>
          ) : (
            <span className="text-gray-700">—</span>
          )}
          <a href={problem.leetcodeUrl} target="_blank" rel="noreferrer" className="text-gray-500 hover:text-blue-400 transition-colors">
            <ExternalLink size={18} />
          </a>
        </div>
      </div>
    </div>
  );
};

export const PatternAccordion = ({ pattern, problems, userProgress, onToggleProblem }) => {
  const [isOpen, setIsOpen] = useState(true);
  
  const completedCount = problems.filter(p => userProgress[p.id]).length;
  
  return (
    <div className="mb-4 bg-[#11181a] border border-gray-800 rounded-xl overflow-hidden">
      <button 
        onClick={() => setIsOpen(!isOpen)}
        className="w-full flex items-center justify-between p-4 bg-[#141b1d] hover:bg-gray-800 transition-colors focus:outline-none"
      >
        <div className="flex items-center gap-4">
          <h3 className="text-lg font-semibold text-gray-100">{pattern.name}</h3>
          <span className="text-xs font-medium bg-gray-800 text-gray-300 px-2 py-1 rounded-full">
            {completedCount} / {problems.length}
          </span>
        </div>
        {isOpen ? <ChevronUp size={20} className="text-gray-400" /> : <ChevronDown size={20} className="text-gray-400" />}
      </button>
      
      {isOpen && (
        <div className="p-0">
          <div className="flex items-center justify-between py-2 px-4 border-b border-gray-800 text-xs font-semibold text-gray-500 uppercase tracking-wider">
            <div className="flex gap-4">
              <span>Done</span>
              <span className="ml-5">Problem</span>
            </div>
            <div className="flex gap-6">
              <span className="w-16 text-center">Difficulty</span>
              <div className="flex gap-4 w-16 justify-end">
                <span>Video</span>
                <span>Practice</span>
              </div>
            </div>
          </div>
          {problems.map((problem) => (
            <ProblemRow 
              key={problem.id} 
              problem={problem} 
              isCompleted={!!userProgress[problem.id]}
              onToggle={onToggleProblem}
            />
          ))}
        </div>
      )}
    </div>
  );
};
