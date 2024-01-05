import React, {useState} from 'react';
import HomeBase from '../../assets/HomeBase.svg';
import { Link } from 'react-router-dom';
import '../styles/SignupStyles.css';
import { useNavigate } from "react-router-dom";



export default function Signup() {
    
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [formSubmitted, setFormSubmitted] = useState(false);

    const handleNameChange = (event) => {
        // const newName = event.target.value;
        
        // if (formSubmitted && (/[^a-zA-Z0-9 ]/.test(newName) || newName.length < 4 || newName.length > 20)) {
        //     alert("Username must be between 4-20 letters and not contain special characters");
        //     return;
        // }
        // setName(newName);
        //merge items comment
        setName(event.target.value);
    }

    const handlePasswordChange = (event) => {
        // const newPassword = event.target.value;

        // if (formSubmitted && (newPassword.length < 8 || newPassword.length > 20)) {
        //     alert("Password must be between 8 and 20 characters");
        //     return;
        // }
        setPassword(event.target.value);
    }

    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    }

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    }

    const isValidEmail = (email) => {
        const emailValidation = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailValidation.test(email);
    }

    const handleSubmit = (event) => {
        let isValid = true;
        // const navigate = useNavigate();

      
        if (/[^a-zA-Z0-9 ]/.test(name) || name.length < 4 || name.length > 20) {
          alert("Username must be between 4-20 letters and not contain special characters");
          isValid = false;
        }
      
        if (!isValidEmail(email)) {
          alert("Invalid email format");
          isValid = false;
        }
      
        if (password.length < 4 || password.length > 20) {
          alert("Password must be between 4 and 20 characters");
          isValid = false;
        }
      
        if (password !== confirmPassword) {
          alert("Passwords do not match");
          isValid = false;
        }
      
        if (isValid) {
          alert("Form submitted");
          setFormSubmitted(true);

        //   navigate('/edit');
        } 
      
        event.preventDefault();
      };

    return (
        <div className='App'>
            <form onSubmit={handleSubmit}>
                <div className="formInfo">
                    <label>
                        Username:
                        <input type="text" value={name} onChange={handleNameChange} />
                    </label>
                </div>
                <div className="formInfo">
                    <label>
                        Email:
                        <input type="email"  value={email} onChange={handleEmailChange}/>
                    </label>
                </div>
                <div className="formInfo">
                    <label>
                        Password:
                        <input type="password" value={password} onChange={handlePasswordChange} />
                    </label>
                </div>
                 <div className="formInfo">
                    <label>
                        Confirm Password:
                        <input type="password" value={confirmPassword} onChange={handleConfirmPasswordChange} />
                    </label>
                </div>
                    <input type="submit" value="Submit" />
            </form>
        </div>
    );
}


