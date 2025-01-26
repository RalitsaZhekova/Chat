package com.fmi.chat_app.services;

import com.fmi.chat_app.entities.User;
import com.fmi.chat_app.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(User user) {
        return userRepository.create(user);
    }

    public List<User> getAllUsers() {
        return userRepository.fetchAll();
    }

    public User getUserById(int id) {
        return userRepository.fetchById(id);
    }

    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    public boolean deleteUser(int id) {
        return userRepository.delete(id);
    }

    public List<User> searchUsers(String query) {
        return userRepository.searchUsers(query);
    }

    public int getTotalUserFriendsCount(int userId) {
        List<User> allFriends = userRepository.countUserFriends(userId);
        return allFriends.size();
    }

    public Map<String, Object> getPaginatedUserFriends(int userId, int page, int rowsPerPage) {
        int offset = (page - 1) * rowsPerPage;

        List<User> paginatedFriends = userRepository.fetchUserFriends(userId, rowsPerPage, offset);

        int totalRecords = getTotalUserFriendsCount(userId);

        int totalPages = (int) Math.ceil( (double) totalRecords / rowsPerPage);

        Map<String, Object> response = new HashMap<>();
        response.put("friends", paginatedFriends);
        response.put("pagination", Map.of(
                "currentPage", page,
                "totalPages", totalPages,
                "totalRecords", totalRecords,
                "rowsPerPage", rowsPerPage
        ));

        return response;
    }
}
