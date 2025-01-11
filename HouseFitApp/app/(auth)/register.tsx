import React, { useState } from 'react';
import { View, TextInput, Pressable, StyleSheet, Alert, Text, SafeAreaView, TouchableOpacity } from 'react-native';
import { router } from 'expo-router';
import { useFonts } from 'expo-font';
import { authService } from '@/src/services/authService';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function RegisterScreen() {
  const [fontsLoaded] = useFonts({
    'Poppins-Regular': require('@/assets/fonts/Poppins/Poppins-Regular.ttf'),
    'Poppins-Bold': require('@/assets/fonts/Poppins/Poppins-Bold.ttf'),
  });
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const validateInputs = () => {
    if (!username || !password || !confirmPassword) {
      Alert.alert('Error', 'Please fill in all fields');
      return false;
    }

    if (username.length < 3) {
      Alert.alert('Error', 'Username must be at least 3 characters long');
      return false;
    }

    if (password.length < 6) {
      Alert.alert('Error', 'Password must be at least 6 characters long');
      return false;
    }

    if (password !== confirmPassword) {
      Alert.alert('Error', 'Passwords do not match');
      return false;
    }

    return true;
  };

  const handleRegister = async () => {
    if (!validateInputs()) return;
  
    try {
      setLoading(true);
      const response = await authService.register(username, password);
      
      if (response.token) {
        await AsyncStorage.setItem('token', response.token);
        
        try {
          const userDetails = await authService.getUserDetails(username);
          await AsyncStorage.setItem('user', JSON.stringify(userDetails));
          
          Alert.alert(
            'Success', 
            'Registration successful!',
            [
              {
                text: 'OK',
                onPress: () => router.replace('/(tabs)/index/home')
              }
            ]
          );
        } catch (error) {
          Alert.alert('Error', 'Failed to fetch user details');
        }
      }
    } catch (error: any) {
        Alert.alert('Registration Failed', error.message);
    }
  };

  if (!fontsLoaded) {
    return null;
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.mainContent}>
        <Text style={styles.title}>Create Account</Text>
        
        <TextInput 
          style={styles.input}
          placeholder="Username"
          placeholderTextColor="#666"
          value={username}
          onChangeText={setUsername}
          autoCapitalize="none"
        />
        
        <TextInput 
          style={styles.input}
          placeholder="Password"
          secureTextEntry
          placeholderTextColor="#666"
          value={password}
          onChangeText={setPassword}
        />

        <TextInput 
          style={styles.input}
          placeholder="Confirm Password"
          secureTextEntry
          placeholderTextColor="#666"
          value={confirmPassword}
          onChangeText={setConfirmPassword}
        />

        <TouchableOpacity 
          style={styles.button}
          onPress={handleRegister}
        >
          <Text style={styles.buttonText}>
            Sign Up
          </Text>
        </TouchableOpacity>
      </View>

      <View style={styles.bottomContainer}>
        <View style={styles.bottomTextContainer}>
          <Text style={styles.linkText}>Already have an account? </Text>
          <TouchableOpacity onPress={() => router.push('/login')}>
            <Text style={styles.linkTextHighlight}>Login</Text>
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  mainContent: {
    flex: 1,
    padding: 20,
    justifyContent: 'center',
  },
  title: {
    textAlign: 'center',
    marginBottom: 40,
    fontFamily: 'Poppins-Bold',
    fontSize: 24,
    color: '#000',
  },
  input: {
    height: 50,
    borderWidth: 1,
    borderColor: '#E0E0E0',
    borderRadius: 8,
    paddingHorizontal: 15,
    marginBottom: 15,
    fontSize: 16,
    backgroundColor: '#F5F5F5',
    fontFamily: 'Poppins-Regular',
    color: '#000',
  },
  button: {
    backgroundColor: '#000',
    height: 50,
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontFamily: 'Poppins-Bold',
  },
  bottomContainer: {
    padding: 20,
    borderTopWidth: 1,
    borderTopColor: '#E0E0E0',
  },
  bottomTextContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  linkText: {
    color: '#000',
    fontSize: 14,
    fontFamily: 'Poppins',
  },
  linkTextHighlight: {
    color: '#000',
    fontSize: 14,
    fontFamily: 'Poppins-Bold',
    textDecorationLine: 'underline',
    textDecorationStyle: 'solid',
  },
});