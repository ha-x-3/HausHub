import { useContext } from 'react';
import { AuthContext } from '../../components/AuthContext';

const useAuthContext = () => {
    const context = useContext(AuthContext);
  
    if (!context) {
      return false; // Or a loading state indicator
    }
  
    const { isAuthenticated = false } = context;
    return isAuthenticated;
  };
  
  export default useAuthContext;