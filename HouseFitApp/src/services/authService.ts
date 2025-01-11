import api from '../config/axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { User } from '@/src/types/auth';

export const authService = {

  async getUserDetails(username: string): Promise<User> {
    try {
      const token = await AsyncStorage.getItem('token');
      const response = await api.get(`/api/user/${username}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      return response.data;
    } catch (error: any) {
      console.log('Error fetching user details:', error);
      throw error.response?.data || error.message;
    }
  },
  
  login: async (username: string, password: string) => {
    try {
      const response = await api.post('/auth/login', {
        username,
        password,
      });
      return response.data;
    } catch (error: any) {
      throw error.response?.data || error.message;
    }
  },

  register: async (username: string, password: string) => {
    try {
      const response = await api.post('/auth/signup', {
        username,
        password,
      });
      return response.data;
    } catch (error: any) {
      throw error.response?.data || error.message;
    }
  },

  async setDefaultTrainerAvatar(trainerId: number): Promise<User> {
    const token = await AsyncStorage.getItem('token');
    const avatarUrl = '/assets/images/default-avatar.png';
    const response = await api.put(
      `/api/user/${trainerId}/avatar`,
      { avatarUrl },
      { headers: { Authorization: `Bearer ${token}` }}
    );
    return response.data;
  },
};