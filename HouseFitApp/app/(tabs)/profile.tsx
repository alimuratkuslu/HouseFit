import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { useAuth } from '@/src/hooks/useAuth';
import { ScreenLayout } from '@/src/components/ScreenLayout';

export default function ProfileScreen() {
  const { user, logout } = useAuth();

  return (
    <ScreenLayout>
      <View style={styles.header}>
        <View style={styles.avatarContainer}>
          <Image
            source={{ uri: user?.avatar || 'https://via.placeholder.com/150' }}
            style={styles.avatar}
          />
        </View>
        <Text style={styles.name}>{user?.username}</Text>
        <Text style={styles.userType}>{user?.userType}</Text>
      </View>

      <View style={styles.content}>
        <TouchableOpacity style={styles.menuItem}>
          <Text style={styles.menuText}>Edit Profile</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.menuItem}>
          <Text style={styles.menuText}>Settings</Text>
        </TouchableOpacity>

        <TouchableOpacity 
          style={[styles.menuItem, styles.logoutButton]} 
          onPress={logout}
        >
          <Text style={[styles.menuText, styles.logoutText]}>Logout</Text>
        </TouchableOpacity>
      </View>
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  header: {
    alignItems: 'center',
    paddingVertical: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  avatarContainer: {
    width: 100,
    height: 100,
    borderRadius: 50,
    overflow: 'hidden',
    marginBottom: 15,
  },
  avatar: {
    width: '100%',
    height: '100%',
  },
  name: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
    color: '#000',
  },
  userType: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    marginTop: 5,
  },
  content: {
    paddingVertical: 20,
  },
  menuItem: {
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  menuText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#000',
  },
  logoutButton: {
    marginTop: 20,
    borderBottomWidth: 0,
  },
  logoutText: {
    color: '#FF3B30',
  },
});