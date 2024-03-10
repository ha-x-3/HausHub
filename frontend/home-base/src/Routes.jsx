import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import NavigationBar from './components/NavigationBar';
import Landing from './components/views/Landing';
import Signup from './components/views/Signup';
import Login from './components/views/Login';
import Edit from './components/views/Edit';
import FilterChangeCard from './components/views/FilterChangeCard';
import EditFilter from './components/views/EditFilter';
import NotificationHistory from './components/views/NotificationHistory';
import FilterChangeHistory from './components/views/FilterChangeHistory';
import EditUser from './components/views/EditUser';
import { useAuth } from './components/AuthContext';


const Routing = () => {

  const { user, checkAuthentication } = useAuth();

  useEffect(() => {
    checkAuthentication();
  }, [checkAuthentication]);

  return (
    <Router>
      <NavigationBar />
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        {user ? (
          <>
            <Route path="/edit" element={<Edit />} />
            <Route path="/filter-change" element={<FilterChangeCard />} />
            <Route path="/editFilter/:id" element={<EditFilter />} />
            <Route path="/notification-history" element={<NotificationHistory />} />
            <Route path="/filter-history" element={<FilterChangeHistory />} />
            <Route path="/edit-user/:id" element={<EditUser />} />
          </>
        ) : (
          <Route path="/login" element={<Login />} />
        )}
      </Routes>
    </Router>
  );
};

export default Routing;