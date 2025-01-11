import { Stack } from 'expo-router';

export default function AppointmentsLayout() {
  return (
    <Stack>
      <Stack.Screen 
        name="index" 
        options={{ 
          title: 'My Sessions',
          headerShown: false 
        }} 
      />
      <Stack.Screen 
        name="new" 
        options={{ 
          title: 'Book New Session',
          presentation: 'modal'
        }} 
      />
    </Stack>
  );
} 