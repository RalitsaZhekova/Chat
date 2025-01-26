package com.fmi.chat_app.controllers;

import com.fmi.chat_app.entities.User;
import com.fmi.chat_app.http.AppResponse;
import com.fmi.chat_app.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userService.createUser(user)) {
            return AppResponse.success()
                    .withMessage("User created successfully")
                    .build();
        }
        return AppResponse.error()
                .withMessage("Failed to create user")
                .build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return AppResponse.success()
                .withData(users)
                .build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return AppResponse.error()
                    .withMessage("User not found")
                    .build();
        }
        return AppResponse.success()
                .withDataAsArray(user)
                .build();
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if (userService.updateUser(user)) {
            return AppResponse.success()
                    .withMessage("User updated successfully")
                    .build();
        }
        return AppResponse.error()
                .withMessage("Failed to update user")
                .build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        if (userService.deleteUser(id)) {
            return AppResponse.success()
                    .withMessage("User deleted successfully")
                    .build();
        }
        return AppResponse.error()
                .withMessage("Failed to delete user")
                .build();
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        return AppResponse.success()
                .withData(users)
                .build();
    }

    @GetMapping("/users/{id}/friends")
    public ResponseEntity<?> getUserFriends(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int rowsPerPage) {

        Map<String, Object> response = userService.getPaginatedUserFriends(id, page, rowsPerPage);
        return ResponseEntity.ok(response);
    }
}
