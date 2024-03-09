import React from 'react';
import './App.css';
import Routes from './Routes';
import { AuthProvider } from './components/AuthContext';
import useAuthContext from './components/hooks/useAuthContext';

function App() {
  const isAuthenticated = useAuthContext();

  return (
    <AuthProvider>
      <div className='App'>
        <Routes isAuthenticated={isAuthenticated} />
      </div>
    </AuthProvider>
  );
}

export default App;