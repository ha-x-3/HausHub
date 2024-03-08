import React, { useState, useEffect } from 'react';
import './App.css'
import Routes from './Routes'
import { AuthProvider } from './components/AuthContext';

function App() {

  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('user');
    if (token) {
      setUser(token);
    }
  }, []);
  
  return (
    <AuthProvider>
      <div className='App'>
        <Routes user={user} />
      </div>
    </AuthProvider>
  )
}

export default App;