package com.fmi.chat_app.services;

import com.fmi.chat_app.repositories.FriendsRepository;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    private final FriendsRepository friendRepository;

    public FriendService(FriendsRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public boolean addFriend(int userId1, int userId2) {
        return friendRepository.addFriend(userId1, userId2);
    }

    public boolean removeFriend(int id) {
        return friendRepository.removeFriend(id);
    }

    public boolean doesFriendshipExist(int userId1, int userId2) {
        return friendRepository.doesFriendshipExist(userId1, userId2);
    }

}
