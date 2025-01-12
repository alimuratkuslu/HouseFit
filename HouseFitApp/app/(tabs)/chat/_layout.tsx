import { Stack } from 'expo-router';

export default function ChatLayout() {
  return (
    <Stack>
      <Stack.Screen
        name="index"
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="[id]"
        options={{
          presentation: 'card',
          headerTitle: 'Chat',
          headerBackTitle: 'Back',
        }}
      />
      <Stack.Screen
        name="new"
        options={{
          presentation: 'card',
          headerTitle: 'New Chat',
          headerBackTitle: 'Back',
        }}
      />
    </Stack>
  );
} 