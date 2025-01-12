import React from 'react';
import { useState, useEffect, createContext, useContext } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { User } from '../types/auth';
import api from '../config/axios';
import { router } from 'expo-router';
import { authService } from '../services/authService';
import { Alert } from 'react-native';

interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  register: (username: string, password: string) => Promise<void>;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

const TOKEN_KEY = '@auth_token';
const USER_KEY = '@auth_user';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    try {
      console.log('Loading user data...');
      const token = await AsyncStorage.getItem('token');
      const userData = await AsyncStorage.getItem('user');

      console.log('Stored token:', token);
      console.log('Stored user data:', userData);

      if (token && userData) {
        const parsedUser = JSON.parse(userData);
        console.log('Parsed user:', parsedUser);
        setUser(parsedUser);
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      } else {
        console.log('No stored session found');
      }
    } catch (error) {
      console.error('Error loading auth state:', error);
    } finally {
      setLoading(false);
    }
  };

  const login = async (username: string, password: string) => {
    try {
      setLoading(true);
      console.log('Attempting login for user:', username);
      const response = await authService.login(username, password);
      console.log('Login response:', response);

      if (response.token) {
        await AsyncStorage.setItem(TOKEN_KEY, response.token);
        console.log('Token saved successfully');

        try {
          const userDetails = await authService.getUserDetails(username);
          console.log('User details fetched:', userDetails);
          await AsyncStorage.setItem(USER_KEY, JSON.stringify(userDetails));
          console.log('User details saved successfully');

          setUser(userDetails);
          api.defaults.headers.common['Authorization'] = `Bearer ${response.token}`;
          router.replace('/(tabs)/index');
        } catch (userError) {
          console.error('Failed to fetch user details:', userError);
          await AsyncStorage.removeItem(TOKEN_KEY);
          throw new Error('Failed to fetch user details');
        }
      }
    } catch (error) {
      console.error('Login error:', error);
      setLoading(false);
      Alert.alert('Error', 'Failed to login. Please try again.');
    }
  };

  const logout = async () => {
    try {
      await Promise.all([
        AsyncStorage.removeItem(TOKEN_KEY),
        AsyncStorage.removeItem(USER_KEY)
      ]);

      setUser(null);
      delete api.defaults.headers.common['Authorization'];
      router.replace('/(auth)/login');
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const register = async (username: string, password: string) => {
    try {
      setLoading(true);
      const response = await authService.register(username, password);

      if (response.token) {
        await AsyncStorage.setItem(TOKEN_KEY, response.token);
        
        try {
          const userDetails = await authService.getUserDetails(username);
          await AsyncStorage.setItem(USER_KEY, JSON.stringify(userDetails));

          setUser(userDetails);
          api.defaults.headers.common['Authorization'] = `Bearer ${response.token}`;
          router.replace('/(tabs)/index');
        } catch (userError) {
          await AsyncStorage.removeItem(TOKEN_KEY);
          throw new Error('Failed to fetch user details');
        }
      }
    } catch (error) {
      console.error('Registration error:', error);
      setLoading(false);
      Alert.alert('Error', 'Failed to register. Please try again.');
    }
  };

  const value = {
    user,
    loading,
    login,
    logout,
    register
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
} 