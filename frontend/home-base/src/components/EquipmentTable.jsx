import React, { useState, useEffect } from "react";
import axios from "axios";
import { Table, Modal } from "react-bootstrap";

const EquipmentTable = () => {
  const [equipment, setEquipment] = useState([]);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteEquipmentId, setDeleteEquipmentId] = useState(null);

  useEffect(() => {
    loadEquipment();
  }, []);

  const loadEquipment = async () => {
    try {
      const token = localStorage.getItem('user');
      const result = await axios.get("http://localhost:8080/api/equipment",
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setEquipment(result.data);
    } catch (error) {
      console.error("Error loading equipment:", error);
    }
  };

  const handleDelete = async (equipmentId) => {
    try {
      const token = localStorage.getItem('user');
      await axios.delete(`http://localhost:8080/api/equipment/${equipmentId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      loadEquipment();
      setShowDeleteModal(false);
    } catch (error) {
      console.error("Error deleting equipment:", error);
    }
  };

  const openDeleteModal = (equipmentId) => {
    setDeleteEquipmentId(equipmentId);
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setShowDeleteModal(false);
  };

  return (
    <div className="table-pane">
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Equipment ID</th>
            <th>Equipment Name</th>
            <th>Max Days Filter Life</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(equipment) && equipment.map((equipmentItem) => (
            <tr key={equipmentItem.id}>
              <td>{equipmentItem.id}</td>
              <td>{equipmentItem.name}</td>
              <td>{equipmentItem.filterLifeDays}</td>
              <td>
                <button
                  className="button"
                  onClick={() => openDeleteModal(equipmentItem.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Modal show={showDeleteModal} onHide={closeDeleteModal}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you want to delete this equipment?
        </Modal.Body>
        <Modal.Footer>
          <button className="button-secondary" onClick={closeDeleteModal}>
            Cancel
          </button>
          <button
            className="button"
            onClick={() => handleDelete(deleteEquipmentId)}
          >
            Delete
          </button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default EquipmentTable;