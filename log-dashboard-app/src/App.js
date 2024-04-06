import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'; 
import Dashboard from './Dashboard'; 

function App() {
  return (
    <Router>
      <div className="App">
        <nav>
          <ul>
            <li><Link to="/">Home</Link></li>
            <li><Link to="/dashboard">Dashboard</Link></li>
          </ul>
        </nav>
        <Routes>
          {/* Define a default route with content */}
          <Route path="/" element={<div>Home Page Content</div>} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Routes> 
      </div>
    </Router>
  );
}

export default App;