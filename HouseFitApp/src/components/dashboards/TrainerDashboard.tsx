import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { router } from 'expo-router';

export default function TrainerDashboard() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Trainer Dashboard</Text>
      
      <TouchableOpacity 
        style={styles.button}
        onPress={() => router.push('/(tabs)/home/set-availability')}
      >
        <Text style={styles.buttonText}>Set New Availability</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    marginBottom: 30,
  },
  button: {
    backgroundColor: '#000',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
  },
});
