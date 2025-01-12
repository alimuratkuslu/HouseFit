import api from '../config/axios';
import { User } from '@/src/types/auth';
import AsyncStorage from '@react-native-async-storage/async-storage';

export interface Availability {
  id: number;
  trainerId: number;
  startTime: string;
  endTime: string;
  isBooked: boolean;
}

export interface Appointment {
  id: number;
  trainerId: number;
  customerId: number;
  appointmentTime: string;
  message: string;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
  trainer: {
    id: number;
    name: string;
    surname: string;
    username: string;
    userType: string;
    avatar?: string;
  };
  customer: {
    id: number;
    name: string;
    surname: string;
    username: string;
    userType: string;
    avatar?: string;
  };
}

export const appointmentService = {
  async getTrainers(): Promise<User[]> {
    const response = await api.get('/api/user/trainers');
    return response.data;
  },

  async getTrainerAvailability(trainerId: number): Promise<Availability[]> {
    try {
      const response = await api.get(`/api/availability/trainer/${trainerId}`);
      return response.data;
    } catch (error) {
      console.error('Error in getTrainerAvailability:', error);
      return [];
    }
  },

  async requestAppointment(trainerId: number, date: string, message: string): Promise<Appointment> {
    try {
      const appointmentDate = new Date(date);
      const formattedDate = `${appointmentDate.getFullYear()}-${
        String(appointmentDate.getMonth() + 1).padStart(2, '0')}-${
        String(appointmentDate.getDate()).padStart(2, '0')}T${
        String(appointmentDate.getHours()).padStart(2, '0')}:${
        String(appointmentDate.getMinutes()).padStart(2, '0')}:00`;

      const response = await api.post('/api/appointment', { 
        trainerId, 
        date: formattedDate,
        message: message || '' // Ensure message is never null
      });
      return response.data;
    } catch (error) {
      console.error('Error creating appointment:', error);
      throw error;
    }
  },

  async getMyAppointments(): Promise<Appointment[]> {
    try {
      const user = await AsyncStorage.getItem('user');
      const userObj = user ? JSON.parse(user) : null;
      
      const endpoint = userObj?.userType === 'TRAINER' 
        ? '/api/appointment/trainer-appointments'
        : '/api/appointment/my-appointments';
      
      const response = await api.get(endpoint);
      return response.data;
    } catch (error) {
      console.error('Error fetching appointments:', error);
      return [];
    }
  },

  async acceptAppointment(appointmentId: number): Promise<Appointment> {
    const response = await api.put(`/api/appointment/${appointmentId}/accept`, null);
    return response.data;
  },

  async declineAppointment(appointmentId: number): Promise<Appointment> {
    const response = await api.put(`/api/appointment/${appointmentId}/decline`, null);
    return response.data;
  },

  async setAvailability(startTime: string, endTime: string): Promise<Availability> {
    const formatDate = (dateString: string) => {
      const date = new Date(dateString);
      return `${date.getFullYear()}-${
        String(date.getMonth() + 1).padStart(2, '0')}-${
        String(date.getDate()).padStart(2, '0')}T${
        String(date.getHours()).padStart(2, '0')}:${
        String(date.getMinutes()).padStart(2, '0')}:00`;
    };

    const response = await api.post('/api/availability', { 
      startTime: formatDate(startTime),
      endTime: formatDate(endTime)
    });
    return response.data;
  },

  async getTrainerAvailabilitiesForDate(trainerId: number, date: string): Promise<Availability[]> {
    try {
      const formattedDate = date.split('T')[0];
      const response = await api.get(`/api/availability/trainer/${trainerId}/date/${formattedDate}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching availabilities:', error);
      return [];
    }
  }
}; 