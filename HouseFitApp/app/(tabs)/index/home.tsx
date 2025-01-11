import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { useAuth } from '@/src/hooks/useAuth';
import { MaterialIcons } from '@expo/vector-icons';
import { Link, router } from 'expo-router';

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
            <TouchableOpacity 
              style={styles.actionButton}
              onPress={() => router.push('/(tabs)/appointments/new')}
            >
              <Text style={styles.actionButtonText}>Book Session</Text>
            </TouchableOpacity>
          </View>
        ) : (
          <>
            <View style={styles.card}>
              <MaterialIcons name="schedule" size={24} color="#000" />
              <Text style={styles.cardTitle}>Training Requests</Text>
              <Text style={styles.cardText}>View and manage your upcoming sessions</Text>
              <TouchableOpacity 
                style={styles.actionButton}
                onPress={() => router.push('/(tabs)/appointments')}
              >
                <Text style={styles.actionButtonText}>View Sessions</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.trainerActions}>
              <Link href="/(tabs)/index/set-availability" asChild>
                <TouchableOpacity style={[styles.trainerButton, styles.availabilityButton]}>
                  <MaterialIcons name="access-time" size={24} color="#fff" />
                  <Text style={styles.trainerButtonText}>Set Availability</Text>
                </TouchableOpacity>
              </Link>

              <Link href="/(tabs)/index/points" asChild>
                <TouchableOpacity style={[styles.trainerButton, styles.pointsButton]}>
                  <MaterialIcons name="emoji-events" size={24} color="#fff" />
                  <Text style={styles.trainerButtonText}>Manage Points</Text>
                </TouchableOpacity>
              </Link>
            </View>
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
    marginBottom: 20,
  },
  actionButton: {
    backgroundColor: '#000',
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 8,
  },
  actionButtonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 16,
  },
  trainerActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 20,
  },
  trainerButton: {
    flex: 0.48,
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
    borderRadius: 12,
  },
  availabilityButton: {
    backgroundColor: '#000',
  },
  pointsButton: {
    backgroundColor: '#4CAF50',
  },
  trainerButtonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 16,
    marginTop: 10,
    textAlign: 'center',
  },
}); 