package com.fmi.chat_app.controllers;

import com.fmi.chat_app.http.AppResponse;
import com.fmi.chat_app.services.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/friends/{userId1}/{userId2}")
    public ResponseEntity<?> addFriend(@PathVariable int userId1, @PathVariable int userId2) {

        if (userId1 == userId2) {
            return AppResponse.error()
                    .withCode(HttpStatus.BAD_REQUEST)
                    .withMessage("A user cannot add themselves as a friend.")
                    .build();
        }

        if (friendService.doesFriendshipExist(userId1, userId2)) {
            return AppResponse.error()
                    .withCode(HttpStatus.CONFLICT)
                    .withMessage("Friendship already exists.")
                    .build();
        }

        if (friendService.addFriend(userId1, userId2)) {
            return AppResponse.success()
                    .withMessage("Friendship successfully created.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Could not create friendship.")
                .build();
    }

    @DeleteMapping("/friends/{id}")
    public ResponseEntity<?> removeFriend(@PathVariable int id) {
        if (friendService.removeFriend(id)) {
            return AppResponse.success()
                    .withMessage("Friendship successfully removed.")
                    .build();
        }
        return AppResponse.error()
                .withCode(HttpStatus.NOT_FOUND)
                .withMessage("Friendship not found.")
                .build();
    }

}
