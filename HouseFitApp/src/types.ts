import { EffortType } from './services/pointsService';

export interface PointsLog {
  id: string;
  effortType: EffortType;
  points: number;
  dateLogged: string;
  trainer: {
    id: number;
    username: string;
    name: string;
  };
} 