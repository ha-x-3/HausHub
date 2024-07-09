import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import HausWrangler from '../assets/HausWrangler.svg';
import HausWranglerText from '../assets/HausWranglerText.png'
import '../components/styles/NavigationBarStyle.css';
import { useAuth } from './AuthContext';

const NavigationBar = () => {
  const { user, logout, checkAuthentication } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    checkAuthentication();
  }, [checkAuthentication]);

  const handleLogout = async () => {
    try {
      await logout(() => navigate('/login'));
      localStorage.removeItem('user');
    } catch (error) {
      console.error("Error during logout:", error.response?.data?.message || "An unexpected error occurred");
    }
  };

  return (
		<nav className='navbar navbar-expand-sm fixed-top'>
			<a
				className='nav-link'
				href='/'
			>
				<img
					src={HausWrangler}
					width='auto'
					height='50'
					className='d-inline-block'
					alt='HausWrangler Logo'
				/>
				<img
					src={HausWranglerText}
					width='auto'
					height='35'
					className='d-inline-block'
					alt='HausWrangler Text'
					style={{ paddingTop: '3px' }}
				/>
			</a>
			<ul className='navbar-nav mr-auto'>
				<li className='nav-item'>
					<a
						className='nav-link'
						href='/HausWrangler/filter-change'
					>
						Replace Filter
					</a>
				</li>
				<li className='nav-item'>
					<a
						className='nav-link'
						href='/HausWrangler/filter-history'
					>
						Filter History
					</a>
				</li>
				<li className='nav-item'>
					<a
						className='nav-link'
						href='/HausWrangler/notification-history'
					>
						Notification History
					</a>
				</li>
				<li className='nav-item'>
					<a
						className='nav-link'
						href='/HausWrangler/edit'
					>
						Edit
					</a>
				</li>
			</ul>
			<div className='nav-user'>
				{user ? (
					<>
						<span className='nav-item nav-link'>
							Welcome, {user}
						</span>
						<button onClick={handleLogout}>Logout</button>
					</>
				) : (
					<li className='nav-item'>
						<Link
							className='button'
							to='/HausWrangler/login'
						>
							Login
						</Link>
					</li>
				)}
			</div>
		</nav>
  );
};

export default NavigationBar;
