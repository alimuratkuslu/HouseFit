import api from '../config/axios';

export interface PointsLog {
  id: number;
  effortType: string;
  points: number;
  dateLogged: string;
  trainer: {
    id: number;
    username: string;
    name: string;
  };
}

export interface LogEffortDto {
  effortType: string;
}

export const EFFORT_TYPES = [
  'Private lesson',
  'Measuring a customer for the first time',
  'Measuring a customer again',
  'Showing a fitness program',
  'Renewing a customer\'s fitness program',
  'Training session at the gym',
  'Posting on Instagram',
  'Posting Instagram story',
  'Having a customer comment on Google Maps',
  'Signing a reference member to the gym',
  'Winning the weekly quiz'
];

export const pointsService = {
  async logEffort(effortType: string): Promise<PointsLog> {
    const response = await api.post('/api/points/log', { effortType });
    return response.data;
  },

  async getMyLogs(): Promise<PointsLog[]> {
    const response = await api.get('/api/points/my-logs');
    return response.data;
  }
}; 