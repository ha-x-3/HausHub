import React, { useEffect, useState } from 'react'
import axiosInstance from '../Axios';
import { Table } from 'react-bootstrap';
import '../styles/NotificationHistoryStyles.css';

const NotificationHistory = () => {

    const [emailNotifications, setEmailNotifications] = useState([]);

    useEffect(() => {
		fetchEmailNotifications();
	}, []);

	const fetchEmailNotifications = async () => {
		try {
			const response = await axiosInstance.get(
				'/email-notifications/history'
			);
			setEmailNotifications(response.data);
		} catch (error) {
			console.error('Error fetching email notification data:', error);
		}
	};

    return (
        <div className='table-pane'>
            <Table striped hover>
                <thead>
                    <tr>
                        <th>Notification ID</th>
                        <th>Recipient Email</th>
                        <th>Equipment ID</th>
                        <th>Equipment Name</th>
                        <th>Filter ID</th>
                        <th>Sent Timestamp</th>
                    </tr>
                </thead>
                <tbody>
                    {emailNotifications.map((emailNotification) => (
                        <tr key={emailNotification.id}>
                            <td>{emailNotification.id}</td>
                            <td>{emailNotification.recipientEmail}</td>
                            <td>{emailNotification.equipmentId}</td>
                            <td>{emailNotification.equipmentName}</td>
                            <td>{emailNotification.filterId}</td>
                            <td>{new Date(emailNotification.sentTimestamp).toLocaleString()}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    )
};

export default NotificationHistory;
