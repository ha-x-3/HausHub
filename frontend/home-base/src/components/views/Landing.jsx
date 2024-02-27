import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import HausWrangler from '../../assets/HausWrangler.svg';
import '../styles/LandingStyles.css';
import axios from 'axios';
import { useAuth } from '../AuthContext';

const Landing = () => {

  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await axios.get('http://localhost:8080/api/logout');
      logout();
      navigate('/login');
    } catch (error) {
      console.error("Error during logout:", error.response?.data?.message || "An unexpected error occurred");
    }
  };

  return (
    <div className="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column align-items-center">
      <img src={HausWrangler} alt="HausWrangler Logo" className="landing-logo" />
      <h1>HausWrangler</h1>  
      <p>Reminders from your home.</p>
      <Link className="button" to="/signup">Sign Up</Link>
       {user ? (
        <button onClick={handleLogout}>Logout</button>
      ) : (
        <Link className="button" to="/login">Login</Link>
      )}
      <footer>
        <p>
          HausWrangler 2024
        </p>
      </footer>
    </div>
  );
};
  
export default Landing;
