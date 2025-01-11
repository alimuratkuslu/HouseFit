import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, TextInput, ActivityIndicator, Image, Alert } from 'react-native';
import { router } from 'expo-router';
import { User } from '@/src/types/auth';
import { appointmentService, Availability } from '@/src/services/appointmentService';
import { Calendar } from 'react-native-calendars';
import { format } from 'date-fns';

const TrainerCard = ({ trainer, isSelected, onSelect }: { 
  trainer: User; 
  isSelected: boolean; 
  onSelect: () => void;
}) => (
  <TouchableOpacity
    style={[styles.trainerCard, isSelected && styles.selectedTrainerCard]}
    onPress={onSelect}
  >
    {trainer.avatar && (
      <Image
        source={{ uri: trainer.avatar }}
        style={styles.trainerAvatar}
      />
    )}
    <View style={styles.trainerInfo}>
      <Text style={[
        styles.trainerName,
        isSelected && styles.selectedTrainerText
      ]}>
        {trainer.name} {trainer.surname}
      </Text>
    </View>
  </TouchableOpacity>
);

const TimeSlotCard = ({ availability, isSelected, onSelect }: {
  availability: Availability;
  isSelected: boolean;
  onSelect: () => void;
}) => {
  const startTime = new Date(availability.startTime);
  const endTime = new Date(availability.endTime);

  return (
    <TouchableOpacity
      style={[styles.timeSlotCard, isSelected && styles.selectedTimeSlot]}
      onPress={onSelect}
      disabled={availability.isBooked}
    >
      <Text style={[styles.timeSlotText, isSelected && styles.selectedTimeSlotText]}>
        {format(startTime, 'HH:mm')} - {format(endTime, 'HH:mm')}
      </Text>
      {availability.isBooked && (
        <Text style={styles.bookedText}>Booked</Text>
      )}
    </TouchableOpacity>
  );
};

export default function NewAppointmentScreen() {
  const [trainers, setTrainers] = useState<User[]>([]);
  const [selectedTrainer, setSelectedTrainer] = useState<User | null>(null);
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedAvailability, setSelectedAvailability] = useState<Availability | null>(null);
  const [dateAvailabilities, setDateAvailabilities] = useState<Availability[]>([]);

  useEffect(() => {
    loadTrainers();
  }, []);

  useEffect(() => {
    if (selectedTrainer) {
      loadAvailabilities();
    }
  }, [selectedTrainer]);

  const loadTrainers = async () => {
    try {
      setLoading(true);
      const data = await appointmentService.getTrainers();
      setTrainers(data || []);
      setError(null);
    } catch (error) {
      console.error('Error loading trainers:', error);
      setError('Failed to load trainers');
      setTrainers([]);
    } finally {
      setLoading(false);
    }
  };

  const loadAvailabilities = async () => {
    if (!selectedTrainer) return;
    try {
      const data = await appointmentService.getTrainerAvailability(selectedTrainer.id);
      setAvailabilities(data || []);
    } catch (error) {
      console.error('Error loading availabilities:', error);
      setAvailabilities([]);
    }
  };

  const handleDateSelect = async (day: { dateString: string }) => {
    setSelectedDate(day.dateString);
    setSelectedAvailability(null);
    
    if (selectedTrainer) {
      try {
        const availabilities = await appointmentService.getTrainerAvailabilitiesForDate(
          selectedTrainer.id,
          day.dateString
        );
        setDateAvailabilities(availabilities);
      } catch (error) {
        console.error('Error loading availabilities:', error);
        setDateAvailabilities([]);
      }
    }
  };

  const handleSubmit = async () => {
    if (!selectedTrainer || !selectedAvailability) return;
    
    try {
      setLoading(true);
      await appointmentService.requestAppointment(
        selectedTrainer.id,
        selectedAvailability.startTime,
        message
      );
      router.back();
    } catch (error) {
      console.error('Error booking appointment:', error);
      Alert.alert('Error', 'Failed to book appointment');
    } finally {
      setLoading(false);
    }
  };

  const markedDates = availabilities.reduce((acc, availability) => {
    try {
      const date = new Date(availability.startTime);
      const formattedDate = date.toISOString().split('T')[0];
      
      if (!availability.isBooked) {
        acc[formattedDate] = { 
          marked: true, 
          dotColor: 'green'
        };
      }
      return acc;
    } catch (error) {
      console.error('Error processing date:', error);
      return acc;
    }
  }, {} as any);

  if (loading) {
    return (
      <View style={[styles.container, styles.centered]}>
        <ActivityIndicator size="large" color="#000" />
      </View>
    );
  }

  if (error) {
    return (
      <View style={[styles.container, styles.centered]}>
        <Text style={styles.errorText}>{error}</Text>
      </View>
    );
  }

  if (trainers.length === 0) {
    return (
      <View style={[styles.container, styles.centered]}>
        <Text style={styles.messageText}>No trainers available at the moment</Text>
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Book New Session</Text>

      <Text style={styles.sectionTitle}>Select Trainer</Text>
      <ScrollView style={styles.trainersContainer}>
        {trainers.map(trainer => (
          <TrainerCard
            key={trainer.id}
            trainer={trainer}
            isSelected={selectedTrainer?.id === trainer.id}
            onSelect={() => setSelectedTrainer(trainer)}
          />
        ))}
      </ScrollView>

      {selectedTrainer && (
        <>
          <Text style={styles.sectionTitle}>Select Date</Text>
          <Calendar
            markedDates={markedDates}
            onDayPress={handleDateSelect}
          />

          {selectedDate && dateAvailabilities.length > 0 && (
            <>
              <Text style={styles.sectionTitle}>Available Time Slots</Text>
              <View style={styles.timeSlotsContainer}>
                {dateAvailabilities.map((availability) => (
                  <TimeSlotCard
                    key={availability.id}
                    availability={availability}
                    isSelected={selectedAvailability?.id === availability.id}
                    onSelect={() => setSelectedAvailability(availability)}
                  />
                ))}
              </View>
            </>
          )}

          {selectedDate && dateAvailabilities.length === 0 && (
            <Text style={styles.noSlotsText}>
              No available time slots for this date
            </Text>
          )}

          {selectedAvailability && (
            <>
              <Text style={styles.sectionTitle}>Message (Optional)</Text>
              <TextInput
                style={styles.input}
                multiline
                numberOfLines={4}
                value={message}
                onChangeText={setMessage}
                placeholder="Add a message for your trainer..."
              />

              <TouchableOpacity
                style={[styles.button, loading && styles.buttonDisabled]}
                onPress={handleSubmit}
                disabled={loading}
              >
                {loading ? (
                  <ActivityIndicator color="#fff" />
                ) : (
                  <Text style={styles.buttonText}>Request Appointment</Text>
                )}
              </TouchableOpacity>
            </>
          )}
        </>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    marginBottom: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontFamily: 'Poppins-Bold',
    marginTop: 20,
    marginBottom: 10,
  },
  trainersContainer: {
    marginBottom: 20,
  },
  trainerCard: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderRadius: 12,
    backgroundColor: '#f5f5f5',
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#E0E0E0',
  },
  selectedTrainerCard: {
    backgroundColor: '#000',
    borderColor: '#000',
  },
  trainerAvatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    marginRight: 15,
  },
  trainerInfo: {
    flex: 1,
  },
  trainerName: {
    fontFamily: 'Poppins-Bold',
    fontSize: 16,
    color: '#000',
  },
  selectedTrainerText: {
    color: '#fff',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 10,
    height: 100,
    textAlignVertical: 'top',
    fontFamily: 'Poppins-Regular',
  },
  button: {
    backgroundColor: '#000',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 20,
  },
  buttonDisabled: {
    backgroundColor: '#666',
  },
  buttonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 16,
  },
  centered: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#FF3B30',
    textAlign: 'center',
  },
  messageText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    textAlign: 'center',
  },
  timeSlotsContainer: {
    marginTop: 10,
  },
  timeSlotCard: {
    padding: 15,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
    marginBottom: 10,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#ddd',
  },
  selectedTimeSlot: {
    backgroundColor: '#000',
    borderColor: '#000',
  },
  timeSlotText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#000',
  },
  selectedTimeSlotText: {
    color: '#fff',
  },
  bookedText: {
    color: '#FF3B30',
    fontFamily: 'Poppins-Regular',
    fontSize: 14,
  },
  noSlotsText: {
    textAlign: 'center',
    color: '#666',
    fontFamily: 'Poppins-Regular',
    fontSize: 16,
    marginTop: 20,
  },
}); 