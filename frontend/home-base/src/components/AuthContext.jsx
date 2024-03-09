import { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isAuthCheckComplete, setIsAuthCheckComplete] = useState(false);

  useEffect(() => {
    checkAuthentication();
  }, []);

  const checkAuthentication = async () => {
    const token = localStorage.getItem('user');
  
    if (token) {
      try {
        const decodedToken = decodeJWT(token);
        if (decodedToken) {
          const { username } = decodedToken;
          setUser(username);
          setIsAuthenticated(true);
          console.log("isAuthenticated true");
        } else {
          setUser(null);
          setIsAuthenticated(false);
          console.log("isAuthenticated false 1");
        }
      } catch (error) {
        console.error('Error verifying token:', error);
        setUser(null);
        setIsAuthenticated(false);
        console.log("isAuthenticated false 2");
      }
    } else {
      console.log("User not authenticated");
      setUser(null);
      setIsAuthenticated(false);
      console.log("isAuthenticated false 3");
    }

    setIsAuthCheckComplete(true);
    console.log("isAuthCheckComplete is true");
  };

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
        localStorage.setItem('user', token);
        checkAuthentication();
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
      await axios.post(
        'http://localhost:8080/api/logout',
        null,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      localStorage.removeItem('user');
      setUser(null);
      setIsAuthCheckComplete(false);
      console.log('Logout successful');
    } catch (error) {
      console.error('Error logging out:', error);
    }
  };

  const decodeJWT = (token) => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const decodedData = JSON.parse(atob(base64));
      //console.log('Decoded Token Payload:', decodedData);
      const username = decodedData.sub;
      return { username };
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated, isAuthCheckComplete }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};

export { AuthContext };