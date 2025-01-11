import React from 'react';
import { View, TextInput, SafeAreaView, StyleSheet, Pressable, Text } from 'react-native';
import { router } from 'expo-router';
import { useFonts } from 'expo-font';

export default function ForgotPasswordScreen() {
  const [fontsLoaded] = useFonts({
    'Poppins-Regular': require('@/assets/fonts/Poppins/Poppins-Regular.ttf'),
    'Poppins-Bold': require('@/assets/fonts/Poppins/Poppins-Bold.ttf'),
  });

  if (!fontsLoaded) return null;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.mainContent}>
        <Text style={styles.title}>Reset Password</Text>
        
        <TextInput 
          style={styles.input}
          placeholder="Email"
          placeholderTextColor="#666"
          keyboardType="email-address"
          autoCapitalize="none"
        />

        <Pressable style={styles.button}>
          <Text style={styles.buttonText}>Send Reset Link</Text>
        </Pressable>
      </View>

      <View style={styles.bottomContainer}>
        <Pressable onPress={() => router.back()}>
          <Text style={styles.linkText}>Back to Login</Text>
        </Pressable>
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
  linkText: {
    color: '#000',
    textAlign: 'center',
    fontFamily: 'Poppins-Regular',
    fontSize: 14,
  },
});
