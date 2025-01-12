import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { useAuth } from '@/src/hooks/useAuth';
import { MaterialIcons } from '@expo/vector-icons';
import { router } from 'expo-router';

function getOccupancyColor(rate: number): string {
  if (rate < 40) return '#4CAF50';
  if (rate < 70) return '#FFA000';
  return '#F44336';
}

function getOccupancyText(rate: number): string {
  if (rate < 40) return 'Not Crowded';
  if (rate < 70) return 'Moderately Busy';
  return 'Very Crowded';
}

export default function HomeScreen() {
  const { user } = useAuth();
  const occupancyRate = Math.floor(Math.random() * 100);
  const occupancyColor = getOccupancyColor(occupancyRate);
  const occupancyText = getOccupancyText(occupancyRate);

  return (
    <ScreenLayout>
      <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>
        <View style={styles.welcomeContainer}>
          <Text style={styles.welcomeText}>Welcome back,</Text>
          <Text style={styles.nameText}>{user?.name}</Text>
        </View>

        <View style={styles.occupancyContainer}>
          <Text style={styles.occupancyTitle}>Current Gym Occupancy</Text>
          <View style={styles.occupancyContent}>
            <View style={styles.occupancyRateContainer}>
              <View style={[styles.occupancyCircle, { borderColor: occupancyColor }]}>
                <Text style={[styles.occupancyRate, { color: occupancyColor }]}>{occupancyRate}%</Text>
              </View>
              <Text style={[styles.occupancyStatus, { color: occupancyColor }]}>{occupancyText}</Text>
            </View>
            <View style={styles.occupancyInfo}>
              <View style={styles.infoItem}>
                <MaterialIcons name="people" size={24} color={occupancyColor} />
                <Text style={styles.infoText}>Current Status</Text>
              </View>
              <View style={styles.infoItem}>
                <MaterialIcons name="update" size={24} color="#666" />
                <Text style={styles.infoText}>Updated just now</Text>
              </View>
            </View>
          </View>
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
                <TouchableOpacity 
                  style={[styles.trainerButton, styles.availabilityButton]}
                  onPress={() => router.push('/home/set-availability')}
                >
                  <MaterialIcons name="access-time" size={24} color="#fff" />
                  <Text style={styles.trainerButtonText}>Set Availability</Text>
                </TouchableOpacity>

                <TouchableOpacity 
                  style={[styles.trainerButton, styles.pointsButton]}
                  onPress={() => router.push('/home/points')}
                >
                  <MaterialIcons name="emoji-events" size={24} color="#fff" />
                  <Text style={styles.trainerButtonText}>Manage Points</Text>
                </TouchableOpacity>
              </View>
            </>
          )}
        </View>
      </ScrollView>
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  contentContainer: {
    paddingBottom: 20,
  },
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
  occupancyContainer: {
    backgroundColor: '#fff',
    borderRadius: 15,
    padding: 20,
    marginHorizontal: 20,
    marginBottom: 20,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
    elevation: 5,
  },
  occupancyTitle: {
    fontSize: 18,
    fontFamily: 'Poppins-Bold',
    marginBottom: 15,
    textAlign: 'center',
  },
  occupancyContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  occupancyRateContainer: {
    alignItems: 'center',
    flex: 1,
  },
  occupancyCircle: {
    width: 100,
    height: 100,
    borderRadius: 50,
    borderWidth: 8,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 10,
  },
  occupancyRate: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
  },
  occupancyStatus: {
    fontSize: 16,
    fontFamily: 'Poppins-Medium',
  },
  occupancyInfo: {
    flex: 1,
    paddingLeft: 20,
  },
  infoItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
  },
  infoText: {
    marginLeft: 10,
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
}); 