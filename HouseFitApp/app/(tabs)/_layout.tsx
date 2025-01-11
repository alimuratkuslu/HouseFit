import { Tabs } from 'expo-router';
import { MaterialIcons } from '@expo/vector-icons';
import { useAuth } from '@/src/hooks/useAuth';

export default function TabLayout() {
  const { user } = useAuth();

  if (!user) return null;

  const isTrainer = user.userType === 'TRAINER';

  return (
    <Tabs screenOptions={{ headerShown: false }}>
      <Tabs.Screen
        name="home"
        options={{
          title: 'Home',
          tabBarIcon: ({ color }) => (
            <MaterialIcons name="home" size={24} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="appointments"
        options={{
          title: 'My Sessions',
          tabBarIcon: ({ color }) => (
            <MaterialIcons name="event" size={24} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="profile"
        options={{
          title: 'Account',
          tabBarIcon: ({ color }) => (
            <MaterialIcons name="person" size={24} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="index"
        options={{
          href: null,
        }}
      />
      {isTrainer && (
        <>
          <Tabs.Screen
            name="points"
            options={{
              title: 'Reward Points',
              tabBarIcon: ({ color }) => (
                <MaterialIcons name="star" size={24} color={color} />
              ),
            }}
          />
          <Tabs.Screen
            name="set-availability"
            options={{
              title: 'Set Availability',
              tabBarIcon: ({ color }) => (
                <MaterialIcons name="schedule" size={24} color={color} />
              ),
            }}
          />
        </>
      )}
    </Tabs>
  );
}