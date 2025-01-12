import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet, TextInput, TouchableOpacity, FlatList, KeyboardAvoidingView, Platform, ActivityIndicator } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { MaterialIcons } from '@expo/vector-icons';
import { useLocalSearchParams, router } from 'expo-router';
import { useAuth } from '@/src/hooks/useAuth';
import { getChatHistory, sendMessage } from '@/src/services/chatService';
import { ChatMessage } from '@/src/types';
import { format } from 'date-fns';

export default function ChatConversationScreen() {
  const { id } = useLocalSearchParams();
  const { user } = useAuth();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const flatListRef = useRef<FlatList>(null);

  useEffect(() => {
    console.log('Current user state:', user);
    console.log('Current id:', id);
    
    if (user) {
      console.log('Loading messages for user:', user.id);
      loadMessages();
    } else {
      console.log('No user found in auth state');
      setError('Please log in to view messages');
      setLoading(false);
    }
  }, [user, id]);

  const loadMessages = async () => {
    if (!user || !id) {
      setError('Unable to load messages. Missing required information.');
      setLoading(false);
      return;
    }

    try {
      const allMessages = await getChatHistory();
      
      if (!Array.isArray(allMessages)) {
        console.error('Messages is not an array:', allMessages);
        setError('Invalid message format received');
        return;
      }

      const conversationMessages = allMessages.filter(msg => {
        if (!msg.sender || !msg.receiver) {
          console.log('Invalid message format:', msg);
          return false;
        }
        return (
          (msg.sender.id === user.id && msg.receiver.id === Number(id)) ||
          (msg.sender.id === Number(id) && msg.receiver.id === user.id)
        );
      });

      setMessages(conversationMessages);
    } catch (error) {
      console.error('Failed to load messages:', error);
      setError('Failed to load messages. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const handleSend = async () => {
    if (!user || !id || !newMessage.trim()) return;

    try {
      const message = await sendMessage(Number(id), newMessage.trim());
      if (message) {
        setMessages(prev => [...prev, message]);
        setNewMessage('');
        flatListRef.current?.scrollToEnd();
      }
    } catch (error) {
      console.error('Failed to send message:', error);
      // Show error toast or message to user
    }
  };

  const renderMessage = ({ item }: { item: ChatMessage }) => {
    if (!item.sender || !user) return null;
    
    const isOwnMessage = item.sender.id === user.id;

    return (
      <View style={[
        styles.messageContainer,
        isOwnMessage ? styles.ownMessage : styles.otherMessage
      ]}>
        <View style={[
          styles.messageBubble,
          isOwnMessage ? styles.ownBubble : styles.otherBubble
        ]}>
          <Text style={[
            styles.messageText,
            isOwnMessage ? styles.ownMessageText : styles.otherMessageText
          ]}>
            {item.content}
          </Text>
          <Text style={[
            styles.timestamp,
            isOwnMessage ? styles.ownTimestamp : styles.otherTimestamp
          ]}>
            {format(new Date(item.timestamp), 'h:mm a')}
          </Text>
        </View>
      </View>
    );
  };

  if (loading) {
    return (
      <ScreenLayout>
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#000" />
        </View>
      </ScreenLayout>
    );
  }

  if (error) {
    return (
      <ScreenLayout>
        <View style={styles.errorContainer}>
          <MaterialIcons name="error-outline" size={48} color="#666" />
          <Text style={styles.errorText}>{error}</Text>
          <TouchableOpacity style={styles.retryButton} onPress={loadMessages}>
            <Text style={styles.retryButtonText}>Try Again</Text>
          </TouchableOpacity>
        </View>
      </ScreenLayout>
    );
  }

  return (
    <ScreenLayout>
      <KeyboardAvoidingView 
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        style={styles.container}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 90 : 0}
      >
        <FlatList
          ref={flatListRef}
          data={messages}
          renderItem={renderMessage}
          keyExtractor={item => item.id.toString()}
          contentContainerStyle={styles.messagesList}
          onContentSizeChange={() => flatListRef.current?.scrollToEnd()}
          onLayout={() => flatListRef.current?.scrollToEnd()}
        />
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            value={newMessage}
            onChangeText={setNewMessage}
            placeholder="Type a message..."
            multiline
          />
          <TouchableOpacity 
            style={styles.sendButton} 
            onPress={handleSend}
            disabled={!newMessage.trim()}
          >
            <MaterialIcons 
              name="send" 
              size={24} 
              color={newMessage.trim() ? '#000' : '#999'} 
            />
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  messagesList: {
    padding: 20,
  },
  messageContainer: {
    marginVertical: 5,
    flexDirection: 'row',
  },
  ownMessage: {
    justifyContent: 'flex-end',
  },
  otherMessage: {
    justifyContent: 'flex-start',
  },
  messageBubble: {
    maxWidth: '80%',
    padding: 12,
    borderRadius: 20,
  },
  ownBubble: {
    backgroundColor: '#000',
    borderBottomRightRadius: 4,
  },
  otherBubble: {
    backgroundColor: '#f0f0f0',
    borderBottomLeftRadius: 4,
  },
  messageText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
  },
  ownMessageText: {
    color: '#fff',
  },
  otherMessageText: {
    color: '#000',
  },
  timestamp: {
    fontSize: 12,
    marginTop: 4,
  },
  ownTimestamp: {
    color: '#ccc',
    textAlign: 'right',
  },
  otherTimestamp: {
    color: '#666',
  },
  inputContainer: {
    flexDirection: 'row',
    padding: 10,
    borderTopWidth: 1,
    borderTopColor: '#E0E0E0',
    backgroundColor: '#fff',
  },
  input: {
    flex: 1,
    minHeight: 40,
    maxHeight: 100,
    backgroundColor: '#f5f5f5',
    borderRadius: 20,
    paddingHorizontal: 15,
    paddingVertical: 10,
    marginRight: 10,
    fontFamily: 'Poppins-Regular',
  },
  sendButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: '#f5f5f5',
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorText: {
    fontSize: 18,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    textAlign: 'center',
    marginBottom: 20,
  },
  retryButton: {
    padding: 10,
    backgroundColor: '#000',
    borderRadius: 5,
  },
  retryButtonText: {
    fontSize: 16,
    fontFamily: 'Poppins-Regular',
    color: '#fff',
  },
}); 