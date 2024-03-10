import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import HausWrangler from '../assets/HausWrangler.svg';
import '../components/styles/NavigationBarStyle.css';
import { useAuth } from './AuthContext';

const NavigationBar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      localStorage.removeItem('user');
      navigate('/login');
    } catch (error) {
      console.error("Error during logout:", error.response?.data?.message || "An unexpected error occurred");
    }
  };

  return (
    <nav className="navbar navbar-expand-sm fixed-top">
      <a className="nav-link" href="/">
        <img src={HausWrangler} width="50" height="50" className="d-inline-block" alt="HausWrangler Logo" />
        HausWrangler
      </a>
      <ul className="navbar-nav mr-auto">
        <li className="nav-item">
          <a className="nav-link" href="/filter-change">
            Replace Filter
          </a>
        </li>
        <li className="nav-item">
          <a className="nav-link" href="/filter-history">
            Filter History
          </a>
        </li>
        <li className="nav-item">
          <a className="nav-link" href="/notification-history">
            Notification History
          </a>
        </li>
        <li className="nav-item">
          <a className="nav-link" href="/edit">
            Edit
          </a>
        </li>
      </ul>
      <div className="nav-user">
        {user ? (
          <>
            <span className="nav-item nav-link">Welcome, {user}</span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <li className="nav-item">
            <Link className="button" to="/login">Login</Link>
          </li>
        )}
      </div>
    </nav>
  );
};

export default NavigationBar;