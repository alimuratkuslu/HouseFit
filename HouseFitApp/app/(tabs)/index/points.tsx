import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Alert } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { MaterialIcons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { logEffort, getMyLogs, EFFORT_TYPES, POINTS_MAP, EffortType } from '@/src/services/pointsService';
import { PointsLog } from '@/src/types';

export default function PointsScreen() {
  const [pointsLogs, setPointsLogs] = useState<PointsLog[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPointsLogs();
  }, []);

  const loadPointsLogs = async () => {
    try {
      const logs = await getMyLogs();
      setPointsLogs(logs);
    } catch (error) {
      Alert.alert('Error', 'Failed to load points history');
    } finally {
      setLoading(false);
    }
  };

  const handleLogEffort = async (effortType: EffortType) => {
    try {
      const newLog = await logEffort(effortType);
      setPointsLogs([newLog, ...pointsLogs]);
      Alert.alert('Success', 'Effort logged successfully');
    } catch (error) {
      Alert.alert('Error', 'Failed to log effort');
    }
  };

  const totalPoints = pointsLogs.reduce((sum, log) => sum + log.points, 0);

  return (
    <ScreenLayout>
      <ScrollView style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.title}>Points Management</Text>
          <Text style={styles.subtitle}>Track and manage your training points</Text>
        </View>

        <View style={styles.pointsCard}>
          <Text style={styles.pointsLabel}>Total Points</Text>
          <Text style={styles.pointsValue}>{totalPoints}</Text>
        </View>

        <View style={styles.actionsContainer}>
          {EFFORT_TYPES.map((effortType) => (
            <TouchableOpacity 
              key={effortType}
              style={[styles.actionButton, { backgroundColor: '#4CAF50' }]}
              onPress={() => handleLogEffort(effortType)}
            >
              <MaterialIcons name="add-task" size={24} color="#fff" />
              <View style={styles.actionButtonContent}>
                <Text style={styles.actionButtonText}>{effortType}</Text>
                <Text style={styles.pointsText}>+{POINTS_MAP[effortType]} points</Text>
              </View>
            </TouchableOpacity>
          ))}
        </View>

        <View style={styles.historyContainer}>
          <Text style={styles.historyTitle}>Points History</Text>
          {loading ? (
            <Text style={styles.loadingText}>Loading...</Text>
          ) : pointsLogs.length === 0 ? (
            <Text style={styles.emptyText}>No points history yet</Text>
          ) : (
            pointsLogs.map((log) => (
              <View key={log.id} style={styles.historyItem}>
                <View>
                  <Text style={styles.historyDate}>
                    {new Date(log.dateLogged).toLocaleDateString()}
                  </Text>
                  <Text style={styles.historyType}>{log.effortType}</Text>
                </View>
                <Text style={styles.historyPoints}>+{log.points} points</Text>
              </View>
            ))
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
  pointsCard: {
    backgroundColor: '#f5f5f5',
    margin: 20,
    padding: 20,
    borderRadius: 12,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E0E0E0',
  },
  pointsLabel: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
  },
  pointsValue: {
    fontSize: 36,
    fontFamily: 'Poppins-Bold',
    color: '#000',
  },
  actionsContainer: {
    padding: 20,
    gap: 15,
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderRadius: 8,
    gap: 15,
  },
  actionButtonContent: {
    flex: 1,
  },
  actionButtonText: {
    color: '#fff',
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
  },
  pointsText: {
    color: '#fff',
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    marginTop: 4,
  },
  historyContainer: {
    padding: 20,
  },
  historyTitle: {
    fontSize: 20,
    fontFamily: 'Poppins-Bold',
    marginBottom: 15,
  },
  loadingText: {
    textAlign: 'center',
    color: '#666',
    fontFamily: 'Poppins-Regular',
  },
  emptyText: {
    textAlign: 'center',
    color: '#666',
    fontFamily: 'Poppins-Regular',
  },
  historyItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  historyDate: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
  },
  historyType: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    marginTop: 4,
  },
  historyPoints: {
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
    color: '#4CAF50',
  },
}); 