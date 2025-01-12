import { EffortType } from './services/pointsService';

export interface User {
  id: number;
  username: string;
  name: string;
  userType: 'CUSTOMER' | 'TRAINER';
}

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

export interface ChatMessage {
  id: number;
  sender: User;
  receiver: User;
  content: string;
  timestamp: string;
}

export interface ChatConversation {
  user: User;
  lastMessage: ChatMessage;
  unreadCount: number;
} 