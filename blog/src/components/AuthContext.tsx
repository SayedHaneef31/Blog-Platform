import React, { createContext, useContext, useState,  useEffect } from 'react';
import { apiService } from '../services/apiService';


export interface AuthUser {
  id: string;
  name: string;
  email: string;
  avatar?: string;
}

interface AuthContextType {
  isAuthenticated: boolean;
  user: AuthUser | null;
  login: (token: string) => Promise<void>;
  logout: () => void;
  token: string | null;
}

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<AuthUser | null>(null);
  

  const [authState, setAuthState] = useState<AuthContextType>({
    isAuthenticated: false,
    user: null,
    token: null,
    login: () => Promise.resolve(), // Placeholder, will be updated below
    logout: () => {},
  });

  const login = async (token: string): Promise<void> => {

    console.log("Inside Login in AuthContext in react");
    
    localStorage.setItem('token', token); // Save token for persistence
    setToken(token);
    console.log("Inside Login in AuthContext in react with token=",token);
    try{
      apiService.setToken(token); // Ensure API service has the latest token
      console.log("Fetching user profile...");
      const userProfile = await apiService.getUserProfile();
      console.log("User profile fetched:", userProfile);
      setUser(userProfile);
      setIsAuthenticated(true);  // Ensure isAuthenticated is updated
      console.log("Login successful, isAuthenticated:", isAuthenticated);
      setAuthState(prevState => ({
      ...prevState,
      isAuthenticated: true,
      token,
    }));
    } catch (error) {
    console.error("Error fetching user profile:", error);
    setIsAuthenticated(false);
    setToken(null);
    setUser(null);
    localStorage.removeItem("token");
    logout(); // Ensure cleanup if profile fetch fails
  }
  };

   // Initialize auth state from token
   useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem("token");
      if (storedToken) {
        try {
          apiService.setToken(storedToken); // Ensure apiService uses the latest token
          const userProfile = await apiService.getUserProfile(); // Fetch user data
          setUser(userProfile);
          setIsAuthenticated(true);
          setToken(storedToken);
        } catch (error) {
                console.error("Invalid token, logging out...");
                localStorage.removeItem("token");
                setToken(null);
                setUser(null);
                setIsAuthenticated(false);
        }
      }
    };
    initializeAuth();
  }, []);

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
    setIsAuthenticated(false); // Ensure state is reset
  };

  // Update apiService token when it changes
  useEffect(() => {
    if (token) {
      // Update axios instance configuration
      const axiosInstance = apiService['api'];
      axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }, [token]);

  const value = {
    isAuthenticated,
    user,
    login,
    logout,
    token
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout, token }}>
    {children}
  </AuthContext.Provider>
);
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext;