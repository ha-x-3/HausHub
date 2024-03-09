import React from 'react';
import './App.css';
import Routing from './Routes';
import { AuthProvider } from './components/AuthContext';

function App() {

  return (
    <AuthProvider>
      <div className='App'>
        <Routing />
      </div>
    </AuthProvider>
  );
}

export default App;