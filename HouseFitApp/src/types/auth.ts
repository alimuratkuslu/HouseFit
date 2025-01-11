export type UserType = 'CUSTOMER' | 'TRAINER' | 'ADMIN';

export interface User {
    id: number;
    username: string;
    name: string;
    surname: string;
    dateOfBirth: string;  
    userType: UserType;
    points: number;
    avatar?: string;
}