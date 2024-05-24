import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
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
            <Route path="/edit" element={<Edit user={user} />} />
            <Route path="/filter-change" element={<FilterChangeCard user={user} />} />
            <Route path="/editFilter/:id" element={<EditFilter user={user} />} />
            <Route path="/notification-history" element={<NotificationHistory user={user} />} />
            <Route path="/filter-history" element={<FilterChangeHistory user={user} />} />
            <Route path="/edit-user/:id" element={<EditUser user={user} />} />
          </>
        ) : (
          <Route path="/login" element={<Login />} />
        )}
      </Routes>
    </Router>
  );
};

export default Routing;