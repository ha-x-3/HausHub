import React, { useState } from 'react';
import AddEquipmentForm from '../AddEquipmentForm';
import AddFilterForm from '../AddFilterForm';
import EditEquipmentTable from '../EditEquipmentTable';
import { UserSettings } from '../UserSettings';
import '../styles/EditStyles.css';

export default function Edit() {
  const [selectedOption, setSelectedOption] = useState(null);

  const renderComponent = () => {
    switch (selectedOption) {
      case 'userSettings':
        return <UserSettings />;
      case 'editEquipment':
        return <EditEquipmentTable />;
      case 'addEquipment':
        return <AddEquipmentForm />;
      case 'addFilter':
        return <AddFilterForm />;
      default:
        return <EditEquipmentTable />;
    }
  };

  const handleOptionClick = (option) => {
    setSelectedOption(option);
  };

  return (
    <div className="container-fluid">
      <div className="menu">
        <ul className="list-group">
          <li
            className="list-group-equipment d-flex justify-content-between align-equipments-center"
            onClick={() => handleOptionClick('userSettings')}
          >
            User Settings
            <span className="badge badge-primary badge-pill">&gt;</span>
          </li>
          <li
            className="list-group-equipment d-flex justify-content-between align-equipments-center"
            onClick={() => handleOptionClick('editEquipment')}
          >
            Edit Filter Information
            <span className="badge badge-primary badge-pill">&gt;</span>
          </li>
          <li
            className="list-group-equipment d-flex justify-content-between align-equipments-center"
            onClick={() => handleOptionClick('addEquipment')}
          >
            Add Equipment
            <span className="badge badge-primary badge-pill">&gt;</span>
          </li>
          <li
            className="list-group-equipment d-flex justify-content-between align-equipments-center"
            onClick={() => handleOptionClick('addFilter')}
          >
            Add Filter
            <span className="badge badge-primary badge-pill">&gt;</span>
          </li>
        </ul>
      </div>
      <div className="right-pane">{renderComponent()}</div>
    </div>
  );
}
