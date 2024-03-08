import React, { useState, useEffect } from 'react';
import './App.css'
import Routes from './Routes'
import { AuthProvider } from './components/AuthContext';

function App() {

  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('user');
    if (token) {
      setUser(token);
      setIsAuthenticated(true);
      console.log(token);
    } else {
      setIsAuthenticated(false);
    }
  }, []);
  
  return (
    <AuthProvider>
      <div className='App'>
        <Routes isAuthenticated={isAuthenticated} />
      </div>
    </AuthProvider>
  );
}

export default App;