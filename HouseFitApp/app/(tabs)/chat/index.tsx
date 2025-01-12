import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, FlatList, ActivityIndicator } from 'react-native';
import { ScreenLayout } from '@/src/components/ScreenLayout';
import { MaterialIcons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useAuth } from '@/src/hooks/useAuth';
import { getChatHistory, groupMessagesByConversation } from '@/src/services/chatService';
import { ChatConversation } from '@/src/types';
import { format } from 'date-fns';

export default function ChatScreen() {
  const { user } = useAuth();
  const [conversations, setConversations] = useState<ChatConversation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (user) {
      loadChatHistory();
    } else {
      setLoading(false);
      setError('Please log in to view your messages');
    }
  }, [user]);

  const loadChatHistory = async () => {
    try {
      setError(null);
      const messages = await getChatHistory();
      if (!messages || messages.length === 0) {
        setConversations([]);
        return;
      }
      const groupedConversations = groupMessagesByConversation(messages, user!.id);
      setConversations(groupedConversations);
    } catch (error) {
      console.error('Failed to load chat history:', error);
      setError('Failed to load messages. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const renderConversation = ({ item }: { item: ChatConversation }) => (
    <TouchableOpacity
      style={styles.conversationItem}
      onPress={() => router.push(`/(tabs)/chat/${item.user.id}`)}
    >
      <View style={styles.avatarContainer}>
        <MaterialIcons 
          name={item.user.userType === 'TRAINER' ? 'fitness-center' : 'person'} 
          size={24} 
          color="#666" 
        />
      </View>
      <View style={styles.conversationContent}>
        <View style={styles.conversationHeader}>
          <Text style={styles.userName}>{item.user.name}</Text>
          <Text style={styles.timestamp}>
            {format(new Date(item.lastMessage.timestamp), 'MMM d, h:mm a')}
          </Text>
        </View>
        <View style={styles.messagePreview}>
          <Text style={styles.lastMessage} numberOfLines={1}>
            {item.lastMessage.content}
          </Text>
          {item.unreadCount > 0 && (
            <View style={styles.unreadBadge}>
              <Text style={styles.unreadCount}>{item.unreadCount}</Text>
            </View>
          )}
        </View>
      </View>
    </TouchableOpacity>
  );

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
        <View style={styles.emptyContainer}>
          <MaterialIcons name="error-outline" size={48} color="#666" />
          <Text style={styles.emptyText}>{error}</Text>
        </View>
      </ScreenLayout>
    );
  }

  return (
    <ScreenLayout>
      <View style={styles.header}>
        <Text style={styles.title}>Messages</Text>
        <TouchableOpacity
          style={styles.newChatButton}
          onPress={() => router.push('/(tabs)/chat/new')}
        >
          <MaterialIcons name="chat" size={24} color="#fff" />
          <Text style={styles.newChatButtonText}>New Chat</Text>
        </TouchableOpacity>
      </View>
      {conversations.length === 0 ? (
        <View style={styles.emptyContainer}>
          <MaterialIcons name="chat-bubble-outline" size={48} color="#666" />
          <Text style={styles.emptyText}>No messages yet</Text>
          <Text style={styles.emptySubtext}>
            Start a conversation by tapping the New Chat button above
          </Text>
          <TouchableOpacity
            style={styles.startChatButton}
            onPress={() => router.push('/(tabs)/chat/new')}
          >
            <MaterialIcons name="chat" size={24} color="#fff" />
            <Text style={styles.startChatButtonText}>Start a Conversation</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={conversations}
          renderItem={renderConversation}
          keyExtractor={(item) => item.user.id.toString()}
          contentContainerStyle={styles.list}
          refreshing={loading}
          onRefresh={loadChatHistory}
        />
      )}
    </ScreenLayout>
  );
}

const styles = StyleSheet.create({
  header: {
    padding: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
  },
  newChatButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#000',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
  },
  newChatButtonText: {
    color: '#fff',
    marginLeft: 8,
    fontFamily: 'Poppins-Medium',
    fontSize: 14,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  list: {
    padding: 20,
  },
  conversationItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#E0E0E0',
  },
  avatarContainer: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: '#f5f5f5',
    justifyContent: 'center',
    alignItems: 'center',
  },
  conversationContent: {
    flex: 1,
    marginLeft: 15,
  },
  conversationHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 5,
  },
  userName: {
    fontSize: 16,
    fontFamily: 'Poppins-Medium',
  },
  timestamp: {
    fontSize: 12,
    color: '#666',
    fontFamily: 'Poppins-Regular',
  },
  messagePreview: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  lastMessage: {
    flex: 1,
    fontSize: 14,
    color: '#666',
    fontFamily: 'Poppins-Regular',
  },
  unreadBadge: {
    backgroundColor: '#000',
    borderRadius: 12,
    minWidth: 24,
    height: 24,
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: 10,
  },
  unreadCount: {
    color: '#fff',
    fontSize: 12,
    fontFamily: 'Poppins-Bold',
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  emptyText: {
    fontSize: 18,
    fontFamily: 'Poppins-Medium',
    marginTop: 20,
    marginBottom: 10,
  },
  emptySubtext: {
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    color: '#666',
    textAlign: 'center',
    marginBottom: 20,
  },
  startChatButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#000',
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderRadius: 25,
    marginTop: 20,
  },
  startChatButtonText: {
    color: '#fff',
    marginLeft: 10,
    fontFamily: 'Poppins-Medium',
    fontSize: 16,
  },
}); 