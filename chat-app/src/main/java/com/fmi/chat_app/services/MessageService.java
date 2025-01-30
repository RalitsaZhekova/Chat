package com.fmi.chat_app.services;

import com.fmi.chat_app.entities.Message;
import com.fmi.chat_app.repositories.ChannelRepository;
import com.fmi.chat_app.repositories.FriendsRepository;
import com.fmi.chat_app.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final FriendsRepository friendsRepository;
    private final ChannelRepository channelRepository;

    public MessageService(MessageRepository messageRepository, FriendsRepository friendsRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.friendsRepository = friendsRepository;
        this.channelRepository = channelRepository;
    }

    public List<Message> getChannelMessages(int channelId) {
        return messageRepository.fetchChannelMessages(channelId);
    }

    public List<Message> getFriendMessages(int userId, int friendId) {
        if (!friendsRepository.doesFriendshipExist(userId, friendId)) {
            return null;
        }
        List<Message> messages = messageRepository.fetchFriendMessages(userId, friendId);
        return messages != null ? messages : new ArrayList<>();
    }

    public boolean sendToChannel(int senderId, int channelId, String content) {
        if (!channelRepository.isUserInChannel(channelId, senderId)) {
            return false;
        }
        return messageRepository.sendMessageToChannel(senderId, channelId, content);
    }

    public boolean sendToFriend(int senderId, int receiverId, String content) {
        if (!friendsRepository.doesFriendshipExist(senderId, receiverId)) {
            return false;
        }
        return messageRepository.sendMessageToFriend(senderId, receiverId, content);
    }

}
