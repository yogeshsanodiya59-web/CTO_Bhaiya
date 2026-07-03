import React from 'react';

export function UpiModal({ isOpen, onClose }) {
  if (!isOpen) return null;

  // Actual UPI ID
  const upiId = "yogessh59@ibl";
  const upiLink = `upi://pay?pa=${upiId}&pn=CTO%20Bhaiya&cu=INR`;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
      <div className="bg-[#0a0e0f] border border-gray-800 rounded-2xl w-full max-w-sm p-6 relative shadow-2xl">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-400 hover:text-white transition-colors"
        >
          <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>

        <div className="text-center mt-2">
          <div className="w-12 h-12 bg-[#FFDD00]/10 rounded-full flex items-center justify-center mx-auto mb-4 border border-[#FFDD00]/20">
            <span className="text-2xl">☕</span>
          </div>
          <h2 className="text-xl font-bold text-white mb-2">Support CTO Bhaiya</h2>
          <p className="text-gray-400 text-sm mb-6 leading-relaxed">
            Scan the QR code with any UPI app (GPay, PhonePe, Paytm) to buy me a coffee!
          </p>

          {/* QR Code */}
          <div className="bg-white p-3 rounded-xl inline-block mb-6 border-4 border-gray-200">
            <img 
              src="/upi-qr.jpg" 
              alt="Scan to pay via UPI" 
              className="w-48 h-48 object-contain"
              onError={(e) => {
                e.target.onerror = null; 
                e.target.src = "https://placehold.co/400?text=Add+upi-qr.jpg+to+public+folder";
              }}
            />
          </div>

          <div className="space-y-3">
            <div className="bg-gray-900/50 border border-gray-800 rounded-lg p-3 flex justify-between items-center">
              <span className="text-emerald-400 font-mono text-sm tracking-wide">{upiId}</span>
              <button 
                onClick={() => {
                  navigator.clipboard.writeText(upiId);
                  alert("UPI ID Copied!");
                }}
                className="text-xs font-semibold bg-gray-800 hover:bg-gray-700 text-gray-300 px-3 py-1.5 rounded transition-colors"
              >
                Copy
              </button>
            </div>
            
            {/* Direct App Link (Works perfectly on mobile devices) */}
            <a 
              href={upiLink}
              className="block w-full bg-emerald-500 hover:bg-emerald-600 text-white font-semibold py-3 rounded-xl transition-colors mt-2 shadow-lg shadow-emerald-500/20"
            >
              Open UPI App
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
