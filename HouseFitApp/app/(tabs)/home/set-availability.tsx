import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Alert } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { MaterialIcons } from '@expo/vector-icons';
import { router } from 'expo-router';
import Calendar from 'react-native-calendars/src/calendar';
import DateTimePickerModal from 'react-native-modal-datetime-picker';

export default function SetAvailabilityScreen() {
  const [selectedDate, setSelectedDate] = useState('');
  const [isStartTimePickerVisible, setStartTimePickerVisible] = useState(false);
  const [isEndTimePickerVisible, setEndTimePickerVisible] = useState(false);
  const [startTime, setStartTime] = useState<Date | null>(null);
  const [endTime, setEndTime] = useState<Date | null>(null);

  const handleDateSelect = (date: any) => {
    setSelectedDate(date.dateString);
  };

  const showStartTimePicker = () => setStartTimePickerVisible(true);
  const showEndTimePicker = () => setEndTimePickerVisible(true);

  const hideStartTimePicker = () => setStartTimePickerVisible(false);
  const hideEndTimePicker = () => setEndTimePickerVisible(false);

  const handleStartTimeConfirm = (date: Date) => {
    setStartTime(date);
    hideStartTimePicker();
  };

  const handleEndTimeConfirm = (date: Date) => {
    setEndTime(date);
    hideEndTimePicker();
  };

  const handleSaveAvailability = () => {
    if (!selectedDate || !startTime || !endTime) {
      Alert.alert('Error', 'Please select date and time slots');
      return;
    }

    if (startTime >= endTime) {
      Alert.alert('Error', 'End time must be after start time');
      return;
    }

    // Here you would typically save the availability to your backend
    Alert.alert('Success', 'Availability saved successfully');
    router.back();
  };

  const formatTime = (date: Date) => {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  return (
    <ScreenLayout>
      <ScrollView style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.title}>Set Your Availability</Text>
          <Text style={styles.subtitle}>Select dates and times when you're available for training sessions</Text>
        </View>

        <Calendar
          onDayPress={handleDateSelect}
          markedDates={{
            [selectedDate]: { selected: true, selectedColor: '#000' },
          }}
          theme={{
            todayTextColor: '#000',
            selectedDayBackgroundColor: '#000',
            selectedDayTextColor: '#fff',
          }}
        />

        <View style={styles.timeContainer}>
          <TouchableOpacity style={styles.timeButton} onPress={showStartTimePicker}>
            <MaterialIcons name="access-time" size={24} color="#000" />
            <Text style={styles.timeButtonText}>
              {startTime ? formatTime(startTime) : 'Select Start Time'}
            </Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.timeButton} onPress={showEndTimePicker}>
            <MaterialIcons name="access-time" size={24} color="#000" />
            <Text style={styles.timeButtonText}>
              {endTime ? formatTime(endTime) : 'Select End Time'}
            </Text>
          </TouchableOpacity>
        </View>

        <TouchableOpacity style={styles.saveButton} onPress={handleSaveAvailability}>
          <Text style={styles.saveButtonText}>Save Availability</Text>
        </TouchableOpacity>

        <DateTimePickerModal
          isVisible={isStartTimePickerVisible}
          mode="time"
          onConfirm={handleStartTimeConfirm}
          onCancel={hideStartTimePicker}
        />

        <DateTimePickerModal
          isVisible={isEndTimePickerVisible}
          mode="time"
          onConfirm={handleEndTimeConfirm}
          onCancel={hideEndTimePicker}
        />
      </ScrollView>
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    padding: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  title: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
  timeContainer: {
    padding: 20,
    gap: 15,
  },
  timeButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#f5f5f5',
    padding: 15,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#E0E0E0',
  },
  timeButtonText: {
    marginLeft: 10,
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
  },
  saveButton: {
    backgroundColor: '#000',
    margin: 20,
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