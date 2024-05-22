import axios from 'axios';

const instance = axios.create({
	baseURL: 'http://localhost:8080/api',
	timeout: 10000,
	headers: {
		'Content-Type': 'application/json',
	},
	withCredentials: true,
});

//Retreive token to build Authorization header
instance.interceptors.request.use(
	(config) => {
		const token = localStorage.getItem('user');
		if (token) {
			config.headers.Authorization = `Bearer ${token}`;
		}
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
);

export default instance;
