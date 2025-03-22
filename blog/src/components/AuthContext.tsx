import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { apiService } from '../services/apiService';


interface AuthUser {
  id: string;
  name: string;
  email: string;
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
  

  // Initialize auth state from token
  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem('token');
      if (storedToken) {
        try {
          apiService.setToken(storedToken); // Ensure apiService uses the latest token
          const userProfile = await apiService.getUserProfile(); // Fetch user data
          setUser(userProfile);
          setIsAuthenticated(true);
          setToken(storedToken);
        } catch (error) {
          // If token is invalid, clear authentication
          localStorage.removeItem('token');
          setIsAuthenticated(false);
          setUser(null);
          setToken(null);
        }
      }
    };

    initializeAuth();
  }, []);

  const [authState, setAuthState] = useState<AuthContextType>({
    isAuthenticated: false,
    user: null,
    token: null,
    login: () => Promise.resolve(), // Placeholder, will be updated below
    logout: () => {},
  });

  const login = async (token: string): Promise<void> => {
    localStorage.setItem('token', token); // Save token for persistence
    setAuthState(prevState => ({
      ...prevState,
      isAuthenticated: true,
      token,
    }));
  };
  // const login = useCallback(async (email: string, password: string) => {
  //   try {
  //     const response = await apiService.login({ email, password });
      
  //     localStorage.setItem('token', response.token);
  //     setToken(response.token);
  //     setIsAuthenticated(true);

  //     // TODO: Add endpoint to fetch user profile after login
  //     // const userProfile = await apiService.getUserProfile();
  //     // setUser(userProfile);
  //   } catch (error) {
  //     throw error;
  //   }
  // }, []);

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
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