import React, { useState, useEffect } from "react";
import axiosInstance from "./Axios";
import { useNavigate } from "react-router-dom";
import './styles/AddFormStyles.css';

const AddFilterForm = () => {

  const [filter, setFilter] = useState({
    id: "",
    location: "",
    length: "",
    width: "",
    height: "",
    dateOfLastChange: "",
    equipment: "",
  });

  const [selectedEquipmentId, setSelectedEquipmentId] = useState("");

  const [equipment, setEquipment] = useState([]);

  useEffect(() => {
    loadEquipment();
  }, []);

  const loadEquipment = async () => {
		try {
			const response = await axiosInstance.get('/equipment');
			setEquipment(response.data);
		} catch (error) {
			console.error('Error loading equipment:', error);
		}
  };

  const [errors, setErrors] = useState({
    location: "",
    length: "",
    width: "",
    height: "",
    dateOfLastChange: "",
    equipment: "",
  });

  function validateForm() {
    let validate = true;
    const errorsCopy = { ...errors };
    const today = new Date().toISOString().split("T")[0]; // Get today's date

    // Validate location
    if (
      filter.location.trim().length >= 2 &&
      filter.location.trim().length <= 75
    ) {
      errorsCopy.location = "";
    } else if (!filter.location.trim()) {
      errorsCopy.location = "Location is required.";
      validate = false;
    } else {
      errorsCopy.location = "Location must be between 2 and 75 characters.";
      validate = false;
    }

    // Validate length
    if (filter.length !== "" && parseFloat(filter.length) > 0) {
      errorsCopy.length = "";
    } else {
      errorsCopy.length = "Length must be a positive number.";
      validate = false;
    }

    // Validate width
    if (filter.width !== "" && parseFloat(filter.width) > 0) {
      errorsCopy.width = "";
    } else {
      errorsCopy.width = "Width must be a positive number.";
      validate = false;
    }

    // Validate height
    if (filter.height !== "" && parseFloat(filter.height) > 0) {
      errorsCopy.height = "";
    } else {
      errorsCopy.height = "Height must be a positive number.";
      validate = false;
    }

    // Validate dateOfLastChange
    if (filter.dateOfLastChange && filter.dateOfLastChange <= today) {
      errorsCopy.dateOfLastChange = "";
    } else {
      errorsCopy.dateOfLastChange =
        "Last Date Filter Was Changed must be today or in the past.";
      validate = false;
    }

    // Validate equipment
    if (selectedEquipmentId !== "") {
      errorsCopy.equipment = "";
    } else {
      errorsCopy.equipment = "Select Equipment is required.";
      validate = false;
    }

    setErrors(errorsCopy);
    return validate;
  }

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFilter({ ...filter, [name]: value });

    if (name === "equipmentId") {
        setSelectedEquipmentId(value);
    }
  };

  const navigate = useNavigate();

  const saveFilter = async (event) => {
		event.preventDefault();

		if (validateForm()) {
			try {
				const response = await axiosInstance.post(
					`/equipment/${selectedEquipmentId}/filters`,
					filter
				);
			} catch (error) {
				console.error('Error:', error);
			} finally {
				navigate(0);
			}
		}
  };

  return (
    <div className="filter-form">
      <form className="filter-form-inputs">
        <div className="form-group">
          <input
            type="hidden"
            name="id"
            id="id"
            value={filter.id}
            onChange={handleChange}
          />
          <label htmlFor="location">Filter Location:</label>
          <input
            type="text"
            id="location"
            name="location"
            value={filter.location}
            onChange={handleChange}
            className={`form-control ${errors.location ? "is-invalid" : ""}`}
          />
          {errors.location && (
            <div className="invalid-feedback">{errors.location}</div>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="length">Filter Length:</label>
          <input
            type="number"
            id="length"
            name="length"
            value={filter.length}
            onChange={handleChange}
            className={`form-control ${errors.length ? "is-invalid" : ""}`}
          />
          {errors.length && (
            <div className="invalid-feedback">{errors.length}</div>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="width">Filter Width:</label>
          <input
            type="number"
            id="width"
            name="width"
            value={filter.width}
            onChange={handleChange}
            className={`form-control ${errors.width ? "is-invalid" : ""}`}
          />
          {errors.width && (
            <div className="invalid-feedback">{errors.width}</div>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="height">Filter Height:</label>
          <input
            type="number"
            id="height"
            name="height"
            value={filter.height}
            onChange={handleChange}
            className={`form-control ${errors.height ? "is-invalid" : ""}`}
          />
          {errors.height && (
            <div className="invalid-feedback">{errors.height}</div>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="dateOfLastChange">
            Last Date Filter Was Changed:
          </label>
          <input
            type="date"
            id="dateOfLastChange"
            name="dateOfLastChange"
            value={filter.dateOfLastChange}
            onChange={handleChange}
            className={`form-control ${
              errors.dateOfLastChange ? "is-invalid" : ""
            }`}
          />
          {errors.dateOfLastChange && (
            <div className="invalid-feedback">{errors.dateOfLastChange}</div>
          )}
        </div>
        <div className="form-group mb-2">
          <label htmlFor="equipmentId">Select Equipment: </label>
          <select
            id="equipmentId"
            name="equipmentId"
            className={`form-select form-control ${
              errors.equipment ? "is-invalid" : ""
            }`}
            value={selectedEquipmentId}
            onChange={handleChange}
          >
            <option value="Select Equipment">Select Equipment</option>
            {Array.isArray(equipment) && equipment.map((item) => {
              return (
                <option key={item.id} value={item.id}>
                  {item.name}
                </option>
              );
            })}
          </select>
          {errors.equipment && (
            <div className="invalid-feedback">{errors.equipment}</div>
          )}
        </div>
        <div className="button-container">
          <button className="button-secondary" onClick={saveFilter}>
            Submit
          </button>
        </div>
      </form>
    </div>
  );
};

export default AddFilterForm;
