import { Stack } from 'expo-router';

export default function IndexLayout() {
  return (
    <Stack screenOptions={{ headerShown: false }}>
      <Stack.Screen name="index" />
      <Stack.Screen 
        name="set-availability" 
        options={{
          presentation: 'modal',
          headerShown: true,
          title: 'Set Availability'
        }}
      />
      <Stack.Screen 
        name="points" 
        options={{
          presentation: 'modal',
          headerShown: true,
          title: 'Points Management'
        }}
      />
    </Stack>
  );
} 