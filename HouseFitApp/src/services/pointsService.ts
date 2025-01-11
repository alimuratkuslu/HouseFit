import { PointsLog } from '../types';
import api from '../config/axios';

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
] as const;

export type EffortType = typeof EFFORT_TYPES[number];

export const POINTS_MAP: Record<EffortType, number> = {
  'Private lesson': 0.5,
  'Measuring a customer for the first time': 3.0,
  'Measuring a customer again': 5.0,
  'Showing a fitness program': 3.0,
  'Renewing a customer\'s fitness program': 5.0,
  'Training session at the gym': 4.0,
  'Posting on Instagram': 10.0,
  'Posting Instagram story': 5.0,
  'Having a customer comment on Google Maps': 10.0,
  'Signing a reference member to the gym': 10.0,
  'Winning the weekly quiz': 5.0
};

export async function getMyLogs(): Promise<PointsLog[]> {
  const response = await api.get('/api/points/my-logs');
  return response.data;
}

export async function logEffort(effortType: EffortType): Promise<PointsLog> {
  const response = await api.post('/api/points/log', { effortType });
  return response.data;
} 