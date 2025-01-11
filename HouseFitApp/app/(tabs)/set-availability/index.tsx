import { useAuth } from '@/src/hooks/useAuth';
import { router } from 'expo-router';
import { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Alert } from 'react-native';
import { Calendar } from 'react-native-calendars';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import { appointmentService } from '@/src/services/appointmentService';
import { format } from 'date-fns';
import { ScreenLayout } from '@/src/components/ScreenLayout';

export default function SetAvailabilityScreen() {
  const { user } = useAuth();
  const [selectedDate, setSelectedDate] = useState('');
  const [isStartTimePickerVisible, setStartTimePickerVisible] = useState(false);
  const [isEndTimePickerVisible, setEndTimePickerVisible] = useState(false);
  const [startTime, setStartTime] = useState<Date | null>(null);
  const [endTime, setEndTime] = useState<Date | null>(null);

  useEffect(() => {
    if (!user || user.userType !== 'TRAINER') {
      router.replace('/(tabs)/home');
    }
  }, [user]);

  const handleDateSelect = (day: { dateString: string }) => {
    setSelectedDate(day.dateString);
    setStartTime(null);
    setEndTime(null);
  };

  const handleStartTimeConfirm = (date: Date) => {
    setStartTime(date);
    setStartTimePickerVisible(false);
  };

  const handleEndTimeConfirm = (date: Date) => {
    setEndTime(date);
    setEndTimePickerVisible(false);
  };

  const handleSaveAvailability = async () => {
    if (!startTime || !endTime) {
      Alert.alert('Error', 'Please select both start and end times');
      return;
    }

    if (startTime >= endTime) {
      Alert.alert('Error', 'End time must be after start time');
      return;
    }

    try {
      const fullStartTime = new Date(selectedDate);
      fullStartTime.setHours(startTime.getHours(), startTime.getMinutes());

      const fullEndTime = new Date(selectedDate);
      fullEndTime.setHours(endTime.getHours(), endTime.getMinutes());

      await appointmentService.setAvailability(
        fullStartTime.toISOString(),
        fullEndTime.toISOString()
      );

      Alert.alert('Success', 'Availability set successfully');
      setSelectedDate('');
      setStartTime(null);
      setEndTime(null);
    } catch (error) {
      console.error('Error setting availability:', error);
      Alert.alert('Error', 'Failed to set availability');
    }
  };

  return (
    <ScreenLayout>
      <ScrollView>
        <View style={styles.welcomeContainer}>
          <Text style={styles.welcomeText}>Schedule Management</Text>
          <Text style={styles.subtitle}>Set your available time slots for training sessions</Text>
        </View>

        <View style={styles.content}>
          <Calendar
            onDayPress={handleDateSelect}
            markedDates={{
              [selectedDate]: { selected: true, selectedColor: '#000' }
            }}
            minDate={new Date().toISOString().split('T')[0]}
          />

          {selectedDate && (
            <View style={styles.timeContainer}>
              <Text style={styles.dateText}>
                Selected Date: {format(new Date(selectedDate), 'MMMM dd, yyyy')}
              </Text>

              <View style={styles.timeButtonsContainer}>
                <TouchableOpacity
                  style={styles.timeButton}
                  onPress={() => setStartTimePickerVisible(true)}
                >
                  <Text style={styles.timeButtonText}>
                    {startTime ? format(startTime, 'HH:mm') : 'Select Start Time'}
                  </Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={styles.timeButton}
                  onPress={() => setEndTimePickerVisible(true)}
                >
                  <Text style={styles.timeButtonText}>
                    {endTime ? format(endTime, 'HH:mm') : 'Select End Time'}
                  </Text>
                </TouchableOpacity>
              </View>

              {startTime && endTime && (
                <TouchableOpacity
                  style={styles.saveButton}
                  onPress={handleSaveAvailability}
                >
                  <Text style={styles.saveButtonText}>Save Availability</Text>
                </TouchableOpacity>
              )}
            </View>
          )}

          <DateTimePickerModal
            isVisible={isStartTimePickerVisible}
            mode="time"
            onConfirm={handleStartTimeConfirm}
            onCancel={() => setStartTimePickerVisible(false)}
          />

          <DateTimePickerModal
            isVisible={isEndTimePickerVisible}
            mode="time"
            onConfirm={handleEndTimeConfirm}
            onCancel={() => setEndTimePickerVisible(false)}
          />
        </View>
      </ScrollView>
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
    fontSize: 32,
    fontFamily: 'Poppins-Bold',
    color: '#000',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
  content: {
    flex: 1,
    paddingTop: 20,
  },
  timeContainer: {
    marginTop: 20,
    padding: 16,
    backgroundColor: '#f5f5f5',
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#E0E0E0',
  },
  dateText: {
    fontSize: 16,
    marginBottom: 16,
    fontFamily: 'Poppins-Bold',
  },
  timeButtonsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 20,
  },
  timeButton: {
    flex: 0.48,
    backgroundColor: '#000',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
  },
  timeButtonText: {
    color: '#fff',
    fontSize: 14,
    fontFamily: 'Poppins-Bold',
  },
  saveButton: {
    backgroundColor: '#4CAF50',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
  },
  saveButtonText: {
    color: '#fff',
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
  },
}); 