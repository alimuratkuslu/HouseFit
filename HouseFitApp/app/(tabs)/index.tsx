import { Redirect } from 'expo-router';
import { useAuth } from '@/src/hooks/useAuth';

export default function Index() {
  const { user } = useAuth();
  return <Redirect href={`/(tabs)/home`} />;
}