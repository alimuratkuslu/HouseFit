import api from '../config/axios';
import { ChatMessage, ChatConversation } from '../types';

export async function sendMessage(receiverId: number, content: string): Promise<ChatMessage> {
  const response = await api.post('/api/chat/send', null, {
    params: { receiverId, content }
  });
  return response.data;
}

export async function getChatHistory(): Promise<ChatMessage[]> {
  const response = await api.get('/api/chat/history');
  return response.data;
}

// Helper function to group messages by conversation
export function groupMessagesByConversation(messages: ChatMessage[], currentUserId: number): ChatConversation[] {
  const conversationsMap = new Map<number, ChatConversation>();

  messages.forEach(message => {
    const otherUser = message.sender.id === currentUserId ? message.receiver : message.sender;
    const existingConversation = conversationsMap.get(otherUser.id);

    if (!existingConversation || new Date(existingConversation.lastMessage.timestamp) < new Date(message.timestamp)) {
      conversationsMap.set(otherUser.id, {
        user: otherUser,
        lastMessage: message,
        unreadCount: message.sender.id !== currentUserId ? 1 : 0
      });
    } else if (message.sender.id !== currentUserId) {
      existingConversation.unreadCount++;
    }
  });

  return Array.from(conversationsMap.values()).sort(
    (a, b) => new Date(b.lastMessage.timestamp).getTime() - new Date(a.lastMessage.timestamp).getTime()
  );
} 