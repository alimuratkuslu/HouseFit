import { useAuth } from '@/src/hooks/useAuth';
import { router } from 'expo-router';
import { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Alert } from 'react-native';
import { pointsService, EFFORT_TYPES, PointsLog } from '@/src/services/pointsService';
import { ScreenLayout } from '@/src/components/ScreenLayout';

export default function PointsScreen() {
  const { user } = useAuth();
  const [pointsLogs, setPointsLogs] = useState<PointsLog[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user || user.userType !== 'TRAINER') {
      router.replace('/(tabs)/home');
    } else {
      loadPointsLogs();
    }
  }, [user]);

  const loadPointsLogs = async () => {
    try {
      setLoading(true);
      const logs = await pointsService.getMyLogs();
      setPointsLogs(logs);
    } catch (error) {
      console.error('Error loading points logs:', error);
      Alert.alert('Error', 'Failed to load points history');
    } finally {
      setLoading(false);
    }
  };

  const handleLogEffort = async (effortType: string) => {
    try {
      setLoading(true);
      await pointsService.logEffort(effortType);
      await loadPointsLogs();
      Alert.alert('Success', 'Effort logged successfully');
    } catch (error) {
      console.error('Error logging effort:', error);
      Alert.alert('Error', 'Failed to log effort');
    } finally {
      setLoading(false);
    }
  };

  const calculateTotalPoints = () => {
    return pointsLogs.reduce((total, log) => total + log.points, 0);
  };

  return (
    <ScreenLayout>
      <ScrollView>
        <View style={styles.welcomeContainer}>
          <Text style={styles.welcomeText}>Your Rewards</Text>
          <View style={styles.pointsContainer}>
            <Text style={styles.pointsLabel}>Total Points:</Text>
            <Text style={styles.pointsValue}>{calculateTotalPoints()}</Text>
          </View>
        </View>

        <View style={styles.content}>
          <Text style={styles.sectionTitle}>Log New Effort</Text>
          <View style={styles.effortTypesContainer}>
            {EFFORT_TYPES.map((type) => (
              <TouchableOpacity
                key={type}
                style={styles.effortButton}
                onPress={() => handleLogEffort(type)}
                disabled={loading}
              >
                <Text style={styles.effortButtonText}>{type}</Text>
              </TouchableOpacity>
            ))}
          </View>

          <Text style={styles.sectionTitle}>Points History</Text>
          <View style={styles.logsContainer}>
            {pointsLogs.map((log) => (
              <View key={log.id} style={styles.logItem}>
                <Text style={styles.logType}>{log.effortType}</Text>
                <View style={styles.logDetails}>
                  <Text style={styles.logPoints}>+{log.points} points</Text>
                  <Text style={styles.logDate}>
                    {new Date(log.dateLogged).toLocaleDateString()}
                  </Text>
                </View>
              </View>
            ))}
            {pointsLogs.length === 0 && (
              <Text style={styles.noLogsText}>No points history yet</Text>
            )}
          </View>
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
    marginBottom: 16,
  },
  content: {
    flex: 1,
    paddingTop: 20,
  },
  pointsContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#f5f5f5',
    padding: 16,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: '#E0E0E0',
    marginTop: 10,
  },
  pointsLabel: {
    fontSize: 18,
    fontFamily: 'Poppins-Regular',
    marginRight: 8,
  },
  pointsValue: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    color: '#4CAF50',
  },
  sectionTitle: {
    fontSize: 20,
    fontFamily: 'Poppins-Bold',
    marginBottom: 16,
  },
  effortTypesContainer: {
    marginBottom: 24,
  },
  effortButton: {
    backgroundColor: '#000',
    padding: 16,
    borderRadius: 12,
    marginBottom: 8,
  },
  effortButtonText: {
    color: '#fff',
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    textAlign: 'center',
  },
  logsContainer: {
    marginBottom: 24,
  },
  logItem: {
    backgroundColor: '#f5f5f5',
    padding: 16,
    borderRadius: 12,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: '#E0E0E0',
  },
  logType: {
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
    marginBottom: 8,
  },
  logDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  logPoints: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#4CAF50',
  },
  logDate: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
  noLogsText: {
    textAlign: 'center',
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    marginTop: 16,
  },
}); 