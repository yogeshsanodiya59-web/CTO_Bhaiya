import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { AuthModal } from './components/AuthModal';
import { SheetPage } from './pages/SheetPage';

function App() {
  const [user, setUser] = useState(null);
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);

  useEffect(() => {
    // Check local storage for existing session
    const token = localStorage.getItem('dsa_token');
    const email = localStorage.getItem('dsa_email');
    if (token && email) {
      setUser({ email });
    }
  }, []);

  const handleSignOut = () => {
    localStorage.removeItem('dsa_token');
    localStorage.removeItem('dsa_email');
    setUser(null);
  };

  return (
    <Router>
      <div className="min-h-screen bg-dark text-white font-sans">
        <header className="sticky top-0 z-50 bg-[#0a0e0f]/90 backdrop-blur-md border-b border-gray-800 p-4">
          <div className="max-w-7xl mx-auto flex justify-between items-center">
            <Link to="/" className="flex items-center gap-2 hover:opacity-80 transition-opacity">
              <span className="text-xl font-bold">
                <span className="text-white">DSA</span>
                <span className="text-emerald-500">.dev</span>
              </span>
              <span className="hidden md:inline text-xs text-gray-400 uppercase tracking-wider ml-4 border-l border-gray-700 pl-4">Sheet Tracker</span>
            </Link>
            <div className="flex items-center gap-6">
              <Link to="/sheet" className="text-sm font-medium text-gray-300 hover:text-white transition-colors">Course</Link>
              {user ? (
                <div className="flex items-center gap-4">
                  <span className="text-sm text-gray-400">{user.email}</span>
                  <button 
                    onClick={handleSignOut}
                    className="bg-gray-800 hover:bg-gray-700 text-white px-4 py-2 rounded-full text-sm font-medium transition-colors border border-gray-700"
                  >
                    Sign out
                  </button>
                </div>
              ) : (
                <button 
                  onClick={() => setIsAuthModalOpen(true)}
                  className="flex items-center space-x-2 bg-emerald-500 hover:bg-emerald-600 text-white px-5 py-2.5 rounded-lg font-medium transition-colors"
                >
                  <span>Sign In</span>
                </button>
              )}
            </div>
          </div>
        </header>

        <main className="max-w-7xl mx-auto p-4 py-8 md:py-12">
           <Routes>
             <Route path="/" element={
               <div className="py-12 md:py-24">
                  <div className="max-w-3xl mb-16">
                    <span className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-xs font-semibold text-emerald-400 mb-8">
                       <span>🚀</span> 90 Day Challenge · 30 patterns · taught by CTO Bhaiya
                    </span>
                    <h1 className="text-5xl md:text-7xl font-extrabold tracking-tight mb-8 leading-tight">
                      Babua DSA Patterns<br/>
                      Course 2025 — <span className="text-emerald-500">think in</span><br/>
                      <span className="text-emerald-500">patterns.</span>
                    </h1>
                    <p className="text-gray-400 text-lg md:text-xl leading-relaxed mb-10 max-w-2xl">
                      Welcome to the DSA Patterns 2025 Course — a 90-day challenge to master the 30 core coding patterns used in FAANG and top product interviews. Instead of solving 1000 random questions, you'll learn how to think in patterns — and solve any problem confidently.
                    </p>
                    <div className="flex items-center gap-4">
                      <Link to="/sheet" className="bg-emerald-500 hover:bg-emerald-600 text-white px-8 py-3.5 rounded-full font-semibold transition-all shadow-lg shadow-emerald-500/25 flex items-center gap-2">
                        Start the challenge
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" /></svg>
                      </Link>
                      {!user && (
                        <button onClick={() => setIsAuthModalOpen(true)} className="bg-gray-800 hover:bg-gray-700 text-white px-8 py-3.5 rounded-full font-semibold transition-colors border border-gray-700">
                          Sign in to track progress
                        </button>
                      )}
                    </div>
                  </div>
               </div>
             } />
             <Route path="/sheet" element={<SheetPage />} />
           </Routes>
        </main>
      </div>

      <AuthModal 
        isOpen={isAuthModalOpen} 
        onClose={() => setIsAuthModalOpen(false)}
        onLoginSuccess={(email) => setUser({ email })}
      />
    </Router>
  );
}

export default App;
