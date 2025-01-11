import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { router } from 'expo-router';
import { User } from '@/src/types/auth';

export function useAuth() {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const [userJson, token] = await Promise.all([
        AsyncStorage.getItem('user'),
        AsyncStorage.getItem('token')
      ]);

      if (!userJson || !token) {
        setUser(null);
      } else {
        setUser(JSON.parse(userJson));
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    try {
      await AsyncStorage.multiRemove(['user', 'token']);
      setUser(null);
      router.replace('/(auth)/login');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return { user, setUser, isLoading, logout, checkAuth };
}
