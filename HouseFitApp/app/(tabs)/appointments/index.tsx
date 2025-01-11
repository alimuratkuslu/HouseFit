import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, ActivityIndicator, SafeAreaView } from 'react-native';
import { router } from 'expo-router';
import { appointmentService, Appointment } from '@/src/services/appointmentService';
import { useAuth } from '@/src/hooks/useAuth';
import { format } from 'date-fns';
import { useFocusEffect } from '@react-navigation/native';
import { ScreenLayout } from '@/src/components/ScreenLayout';

const formatAppointmentDate = (dateString: string | undefined) => {
  try {
    if (!dateString) {
      return 'No date available';
    }

    // Try parsing the date directly
    const date = new Date(dateString);
    if (!isNaN(date.getTime())) {
      return format(date, 'MMM dd, yyyy HH:mm');
    }

    console.error('Invalid date string:', dateString);
    return 'Invalid date format';
  } catch (error) {
    console.error('Error formatting date:', error);
    return 'Error formatting date';
  }
};

export default function AppointmentsScreen() {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    loadAppointments();
  }, []);

  useFocusEffect(
    React.useCallback(() => {
      loadAppointments();
    }, [])
  );

  const loadAppointments = async () => {
    try {
      setLoading(true);
      const data = await appointmentService.getMyAppointments();
      console.log('User type:', user?.userType);
      console.log('Loaded appointments:', JSON.stringify(data, null, 2));
      setAppointments(data || []);
    } catch (error) {
      console.error('Error loading appointments:', error);
      setAppointments([]);
    } finally {
      setLoading(false);
    }
  };

  // Filter appointments based on user type and status
  const getFilteredAppointments = () => {
    if (!appointments) return [];
    
    if (user?.userType === 'TRAINER') {
      // Sort appointments: PENDING first, then others
      return [...appointments].sort((a, b) => {
        if (a.status === 'PENDING' && b.status !== 'PENDING') return -1;
        if (a.status !== 'PENDING' && b.status === 'PENDING') return 1;
        return 0;
      });
    }
    return appointments;
  };

  const handleAccept = async (appointmentId: number) => {
    try {
      await appointmentService.acceptAppointment(appointmentId);
      loadAppointments(); 
    } catch (error) {
      console.error('Error accepting appointment:', error);
    }
  };

  const handleDecline = async (appointmentId: number) => {
    try {
      await appointmentService.declineAppointment(appointmentId);
      loadAppointments(); 
    } catch (error) {
      console.error('Error declining appointment:', error);
    }
  };

  const renderAppointmentCard = (appointment: Appointment) => {
    console.log('Appointment data:', {
      id: appointment.id,
      customer: appointment.customer,
      trainer: appointment.trainer,
      status: appointment.status
    });

    return (
      <View key={appointment.id} style={[
        styles.appointmentCard,
        appointment.status === 'PENDING' && styles.pendingCard
      ]}>
        <View style={styles.appointmentHeader}>
          <Text style={styles.name}>
            {user?.userType === 'CUSTOMER' 
              ? `${appointment.trainer?.name || ''} ${appointment.trainer?.surname || ''}`
              : `${appointment.customer?.name || ''} ${appointment.customer?.surname || ''}`
            }
          </Text>
          <Text style={[
            styles.status,
            appointment.status === 'ACCEPTED' && styles.statusAccepted,
            appointment.status === 'DECLINED' && styles.statusDeclined,
            appointment.status === 'PENDING' && styles.statusPending
          ]}>
            {appointment.status}
          </Text>
        </View>

        <Text style={styles.date}>
          {formatAppointmentDate(appointment.appointmentTime)}
        </Text>

        {appointment.message && (
          <Text style={styles.message}>
            <Text style={styles.messageLabel}>Message: </Text>
            {appointment.message}
          </Text>
        )}

        {user?.userType === 'TRAINER' && appointment.status === 'PENDING' && (
          <View style={styles.actions}>
            <TouchableOpacity 
              style={[styles.actionButton, styles.acceptButton]}
              onPress={() => handleAccept(appointment.id)}
            >
              <Text style={styles.actionButtonText}>Accept</Text>
            </TouchableOpacity>
            <TouchableOpacity 
              style={[styles.actionButton, styles.declineButton]}
              onPress={() => handleDecline(appointment.id)}
            >
              <Text style={styles.actionButtonText}>Decline</Text>
            </TouchableOpacity>
          </View>
        )}
      </View>
    );
  };

  if (loading) {
    return (
      <ScreenLayout>
        <View style={styles.centered}>
          <ActivityIndicator size="large" color="#000" />
        </View>
      </ScreenLayout>
    );
  }

  return (
    <ScreenLayout>
      <ScrollView>
        <View style={styles.header}>
          <Text style={styles.title}>My Sessions</Text>
          {user?.userType === 'CUSTOMER' && (
            <TouchableOpacity 
              style={styles.newButton}
              onPress={() => router.push('/appointments/new')}
            >
              <Text style={styles.newButtonText}>Book New</Text>
            </TouchableOpacity>
          )}
        </View>

        {appointments.length === 0 ? (
          <Text style={styles.noAppointments}>No sessions found</Text>
        ) : (
          <View style={styles.appointmentsList}>
            {getFilteredAppointments().map(renderAppointmentCard)}
          </View>
        )}
      </ScrollView>
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#fff',
  },
  container: {
    flex: 1,
    backgroundColor: '#fff',
    paddingHorizontal: 20,
  },
  centered: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 20,
    paddingTop: 10,
  },
  title: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
  },
  newButton: {
    backgroundColor: '#000',
    padding: 10,
    borderRadius: 8,
  },
  newButtonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 14,
  },
  appointmentsList: {
    paddingBottom: 20,
  },
  appointmentCard: {
    backgroundColor: '#f5f5f5',
    borderRadius: 12,
    padding: 15,
    marginBottom: 15,
    borderWidth: 1,
    borderColor: '#E0E0E0',
  },
  appointmentHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  name: {
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
    flex: 1,
    marginRight: 10,
    color: '#000',
  },
  status: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#F9A825',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
    overflow: 'hidden',
  },
  statusAccepted: {
    color: '#4CAF50',
    backgroundColor: '#E8F5E9',
  },
  statusDeclined: {
    color: '#F44336',
    backgroundColor: '#FFEBEE',
  },
  statusPending: {
    color: '#F9A825',
    backgroundColor: '#FFF8E1',
    fontWeight: 'bold',
  },
  date: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    marginBottom: 10,
  },
  message: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    marginTop: 5,
  },
  actions: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    marginTop: 15,
  },
  actionButton: {
    paddingHorizontal: 15,
    paddingVertical: 8,
    borderRadius: 6,
    marginLeft: 10,
  },
  acceptButton: {
    backgroundColor: '#4CAF50',
  },
  declineButton: {
    backgroundColor: '#F44336',
  },
  actionButtonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
    fontSize: 14,
  },
  noAppointments: {
    textAlign: 'center',
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    marginTop: 20,
  },
  pendingCard: {
    borderColor: '#F9A825',
    borderWidth: 2,
    backgroundColor: '#FFFDE7',
  },
  messageLabel: {
    fontFamily: 'Poppins-Bold',
    color: '#000',
  },
});