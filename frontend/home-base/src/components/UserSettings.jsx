import React, { useEffect, useState } from 'react'
import axiosInstance from './Axios'
import { Table, Modal } from "react-bootstrap";
import "./styles/EditEquipmentTableStyles.css";
import { useNavigate } from 'react-router-dom';

export const UserSettings = () => {

    const [users, setUsers] = useState([]);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteUserId, setDeleteUserId] = useState(null);

    const navigate = useNavigate();

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
			const response = await axiosInstance.get('/users');
			setUsers(response.data);
		} catch (error) {
			console.error('Error loading users:', error);
		}
    };

    const handleEdit = (userId) => {
        navigate(`/edit-user/${userId}`);
    };

    const handleDelete = async (userId) => {
        try {
            await axiosInstance.delete(`/users/${userId}`);
			loadUsers();
			setShowDeleteModal(false);
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };

    const openDeleteModal = (userId) => {
        setDeleteUserId(userId);
        setShowDeleteModal(true);
    };

    const closeDeleteModal = () => {
        setShowDeleteModal(false);
    }

    return (
        <div className="equipment-table-pane">
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {Array.isArray(users) && users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.username}</td>
                            <td>{user.email}</td>
                            <td>
                                <div className='button-container'>
                                    <button className="button-secondary" onClick={() => handleEdit(user.id)}>
                                        Edit
                                    </button>
                                    <button className="button" onClick={() => openDeleteModal(user.id)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <Modal show={showDeleteModal} onHide={closeDeleteModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Delete</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this user?</Modal.Body>
                <Modal.Footer>
                    <button className="button" onClick={closeDeleteModal}>
                        Cancel
                    </button>
                    <button className="button-secondary" onClick={() => handleDelete(deleteUserId)}>
                        Delete
                    </button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}
