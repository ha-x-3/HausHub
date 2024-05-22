import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from './Axios';
import EquipmentTable from "./EquipmentTable";
import './styles/AddFormStyles.css';

const AddEquipmentForm = () => {
  
  const [equipment, setEquipment] = useState({
		id: '',
		name: '',
		filterLifeDays: '',
		users: [],
  });
  const [users, setUsers] = useState([]);
  const [errors, setErrors] = useState({});

  const handleChange = (event) => {
    const { name, value } = event.target;
    setEquipment({ ...equipment, [name]: value });
  };

  const handleUserChange = (event) => {
		const selectedUser = parseInt(event.target.value, 10);
		setEquipment({ ...equipment, users: [selectedUser] });
  };

  const navigate = useNavigate();

  useEffect(() => {
      const fetchUsers = async () => {
        try {
          const response = await axiosInstance.get(
            '/users');
          setUsers(response.data);
        } catch (error) {
          console.error('Error fetching users:', error);
        }
      };

      fetchUsers();
    }, []);

  const fetchUser = async (userId) => {
		try {
			const response = await axiosInstance.get(
				`/users/${userId}`);
			return response.data;
		} catch (error) {
			console.error('Error fetching user:', error);
			return null;
		}
  };

  const validateForm = () => {
    let isValid = true;
    const newErrors = {};

    // Validate name
    if (
      !equipment.name.trim() ||
      equipment.name.trim().length < 2 ||
      equipment.name.trim().length > 50
    ) {
      newErrors.name =
        "Name is required and must be between 2 and 50 characters.";
      isValid = false;
    }

    // Validate filterLifeDays
    if (
      !equipment.filterLifeDays ||
      parseFloat(equipment.filterLifeDays) <= 0
    ) {
      newErrors.filterLifeDays =
        "Filter Life in Days is required and must be a positive number.";
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const saveEquipment = async (event) => {
		event.preventDefault();

		if (validateForm()) {
			const userId = parseInt(equipment.users[0]); // Assuming single user selection

			const user = await fetchUser(userId);

			const equipmentData = {
				...equipment,
        userId: userId
			};

			try {
				const response = await axiosInstance.post(
					`/equipment`,
					equipmentData
				);
			} catch (error) {
				console.error('Error saving equipment:', error);
			} finally {
				navigate(0);
			}
		}
  };

  return (
		<div className='equipment-form'>
			<EquipmentTable />
			<form>
				<div className='form-group'>
					<label htmlFor='name'>Equipment Name:</label>
					<input
						type='text'
						id='name'
						name='name'
						value={equipment.name}
						onChange={handleChange}
						className={`form-control ${
							errors.name ? 'is-invalid' : ''
						}`}
					/>
					{errors.name && (
						<div className='invalid-feedback'>{errors.name}</div>
					)}
				</div>
				<div className='form-group'>
					<label htmlFor='filterLifeDays'>Filter Life in Days:</label>
					<input
						type='number'
						id='filterLifeDays'
						name='filterLifeDays'
						value={equipment.filterLifeDays}
						onChange={handleChange}
						className={`form-control ${
							errors.filterLifeDays ? 'is-invalid' : ''
						}`}
					/>
					{errors.filterLifeDays && (
						<div className='invalid-feedback'>
							{errors.filterLifeDays}
						</div>
					)}
				</div>
				<div className='form-group'>
					<label htmlFor='users'>Assign to Users:</label>
					<select
						id='users'
						name='users'
						value={equipment.users[0]}
						onChange={handleUserChange}
						className='form-control'
					>
						{users.map((user) => (
							<option
								key={user.id}
								value={user.id}
							>
								{user.username}
							</option>
						))}
					</select>
				</div>
				<div className='button-container'>
					<button
						className='button-secondary'
						onClick={(e) => saveEquipment(e)}
					>
						Submit
					</button>
				</div>
			</form>
		</div>
  );
};

export default AddEquipmentForm;
