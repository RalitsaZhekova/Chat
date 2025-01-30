package com.fmi.chat_app.services;

import com.fmi.chat_app.entities.Channel;
import com.fmi.chat_app.repositories.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public boolean createChannel(String name, int ownerId, int isPrivate) {
        return channelRepository.createChannel(name, ownerId, isPrivate);
    }

    public boolean deleteChannel(int channelId, int ownerId) {
        return channelRepository.deleteChannel(channelId, ownerId);
    }

    public List<Channel> fetchUserChannels(int userId) {
        return channelRepository.fetchUserChannels(userId);
    }

    public boolean addUserToChannel(int channelId, int userId, String role) {
        return channelRepository.addUserToChannel(channelId, userId, role);
    }

    public boolean removeUserFromChannel(int channelId, int userId) {
        return channelRepository.removeUserFromChannel(channelId, userId);
    }

    public boolean promoteUserToAdmin(int channelId, int ownerId, int userId) {
        return channelRepository.promoteToAdmin(channelId, ownerId, userId);
    }

    public boolean renameChannel(int channelId, int userId, String newName) {
        return channelRepository.renameChannel(channelId, userId, newName);
    }

    public boolean isOwnerOrAdmin(int channelId, int userId) {
        return channelRepository.isOwnerOrAdmin(channelId, userId);
    }

    public boolean isOwner(int channelId, int userId) {
        return channelRepository.isOwner(channelId, userId);
    }

    public boolean isUserInChannel(int channelId, int userId) {
        return channelRepository.isUserInChannel(channelId, userId);
    }

}
