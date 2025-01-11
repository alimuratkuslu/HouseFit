import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { router } from 'expo-router';

export default function CustomerDashboard() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Welcome Back!</Text>
      
      <View style={styles.statsContainer}>
        <View style={styles.statCard}>
          <Text style={styles.statNumber}>5</Text>
          <Text style={styles.statLabel}>Upcoming Sessions</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statNumber}>12</Text>
          <Text style={styles.statLabel}>Total Sessions</Text>
        </View>
      </View>

      <TouchableOpacity 
        style={styles.bookButton}
        onPress={() => router.push('/(tabs)/appointments/new')}
      >
        <Text style={styles.bookButtonText}>Book New Session</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    marginBottom: 20,
  },
  statsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 30,
  },
  statCard: {
    backgroundColor: '#F5F5F5',
    padding: 20,
    borderRadius: 12,
    width: '48%',
  },
  statNumber: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    color: '#000',
  },
  statLabel: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
  bookButton: {
    backgroundColor: '#000',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
  },
  bookButtonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 16,
  },
});