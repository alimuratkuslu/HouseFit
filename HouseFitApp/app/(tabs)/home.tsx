import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { useAuth } from '@/src/hooks/useAuth';
import { MaterialIcons } from '@expo/vector-icons';
import { router } from 'expo-router';

export default function HomeScreen() {
  const { user } = useAuth();

  return (
    <ScreenLayout>
      <View style={styles.welcomeContainer}>
        <Text style={styles.welcomeText}>Welcome back,</Text>
        <Text style={styles.nameText}>{user?.name}</Text>
      </View>

      <View style={styles.content}>
        {user?.userType === 'CUSTOMER' ? (
          <View style={styles.card}>
            <MaterialIcons name="fitness-center" size={24} color="#000" />
            <Text style={styles.cardTitle}>Ready for your next session?</Text>
            <Text style={styles.cardText}>Book a training session with our expert trainers</Text>
          </View>
        ) : (
          <>
            <View style={styles.card}>
              <MaterialIcons name="schedule" size={24} color="#000" />
              <Text style={styles.cardTitle}>Training Requests</Text>
              <Text style={styles.cardText}>View and manage your upcoming sessions</Text>
            </View>
            <TouchableOpacity 
              style={styles.setAvailabilityButton}
              onPress={() => router.push('/(tabs)/set-availability')}
            >
              <MaterialIcons name="access-time" size={24} color="#fff" />
              <Text style={styles.buttonText}>Set Your Availability</Text>
            </TouchableOpacity>
          </>
        )}
      </View>
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  welcomeContainer: {
    marginTop: 20,
    marginBottom: 30,
    paddingVertical: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  welcomeText: {
    fontSize: 20,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
  nameText: {
    fontSize: 32,
    fontFamily: 'Poppins-Bold',
    color: '#000',
    marginTop: 5,
  },
  content: {
    flex: 1,
    paddingTop: 20,
  },
  card: {
    backgroundColor: '#f5f5f5',
    borderRadius: 15,
    padding: 20,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#E0E0E0',
    alignItems: 'center',
  },
  cardTitle: {
    fontSize: 20,
    fontFamily: 'Poppins-Bold',
    color: '#000',
    marginTop: 15,
    marginBottom: 10,
    textAlign: 'center',
  },
  cardText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    textAlign: 'center',
    lineHeight: 24,
  },
  setAvailabilityButton: {
    backgroundColor: '#000',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 15,
    borderRadius: 12,
    marginTop: 20,
  },
  buttonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 16,
    marginLeft: 10,
  },
}); 