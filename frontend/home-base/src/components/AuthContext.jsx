import { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('user');
    if (token) {
      setUser(token);
    }
  }, []);

  const login = async (userData) => {
    try {
      const response = await axios.post('http://localhost:8080/api/login', userData, {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true
      });
      
      if (response.data.accessToken) {
        const token = response.data.accessToken;
        setUser(token);
        localStorage.setItem('user', token);
      } else {
        console.error('Invalid response format');
      }
    } catch (error) {
      console.error('Error logging in:', error);
      throw error;
    }
  };

  const logout = async () => {
    try {
      const token = localStorage.getItem('user');
      if (!token) {
        // Handle case where token is not found
        console.error('User token not found in localStorage');
        return false;
      }
  
      const response = await axios.post('http://localhost:8080/api/logout', null, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      localStorage.removeItem('user');
      setUser(null);
      return true;
    } catch (error) {
      console.error('Error logging out:', error);
      return false;
    }
  };

  const isAuthenticated = () => {
    return !!user;
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};