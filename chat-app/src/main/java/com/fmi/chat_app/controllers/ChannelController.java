package com.fmi.chat_app.controllers;

import com.fmi.chat_app.entities.Channel;
import com.fmi.chat_app.http.AppResponse;
import com.fmi.chat_app.services.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/channels")
    public ResponseEntity<?> createChannel(
            @RequestParam(required = false) String name,
            @RequestParam int ownerId,
            @RequestParam int isPrivate
    ) {

        if (name == null || name.isEmpty()) {
            name = isPrivate == 1 ? "Private Chat" : "Unnamed Channel";
        }

        if (channelService.createChannel(name, ownerId, isPrivate)) {
            return AppResponse.success()
                    .withMessage("Channel created successfully.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Failed to create channel.")
                .build();
    }

    @DeleteMapping("/channels/{channelId}")
    public ResponseEntity<?> deleteChannel(
            @PathVariable int channelId,
            @RequestParam int ownerId
    ) {
        boolean isOwner = channelService.isOwner(channelId, ownerId);
        if (!isOwner) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("Only the owner can delete this channel.")
                    .build();
        }

        if (channelService.deleteChannel(channelId, ownerId)) {
            return AppResponse.success()
                    .withMessage("Channel deleted successfully.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Failed to delete channel.")
                .build();
    }

    @GetMapping("/channels/of_user/{userId}")
    public ResponseEntity<?> fetchUserChannels(@PathVariable int userId) {
        List<Channel> channels = channelService.fetchUserChannels(userId);

        if (channels.isEmpty()) {
            return AppResponse.error()
                    .withCode(HttpStatus.NOT_FOUND)
                    .withMessage("No channels found for this user.")
                    .build();
        }

        return AppResponse.success()
                .withData(channels)
                .build();
    }

    @PostMapping("/channels/{channelId}/members/add")
    public ResponseEntity<?> addUserToChannel(
            @PathVariable int channelId,
            @RequestParam int requestingUserId,
            @RequestParam int targetUserId,
            @RequestParam String role
    ) {
        if (!channelService.isOwnerOrAdmin(channelId, requestingUserId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("Only owners or admins can add members to this channel.")
                    .build();
        }

        if (channelService.isUserInChannel(channelId, targetUserId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.CONFLICT)
                    .withMessage("User is already a member of the channel.")
                    .build();
        }

        if (channelService.addUserToChannel(channelId, targetUserId, role)) {
            return AppResponse.success()
                    .withMessage("User successfully added to the channel.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Failed to add user to the channel.")
                .build();
    }

    @DeleteMapping("/channels/{channelId}/members/remove")
    public ResponseEntity<?> removeUserFromChannel(
            @PathVariable int channelId,
            @RequestParam int requestingUserId,
            @RequestParam int targetUserId
    ) {
        if (!channelService.isOwnerOrAdmin(channelId, requestingUserId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("Only owners or admins can remove members from this channel.")
                    .build();
        }

        if (!channelService.isUserInChannel(channelId, targetUserId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.NOT_FOUND)
                    .withMessage("User is not a member of the channel.")
                    .build();
        }

        if (channelService.removeUserFromChannel(channelId, targetUserId)) {
            return AppResponse.success()
                    .withMessage("User successfully removed from the channel.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Failed to remove user from the channel.")
                .build();
    }

    @PutMapping("/channels/{channelId}/promote/{targetUserId}")
    public ResponseEntity<?> promoteUserToAdmin(
            @PathVariable int channelId,
            @RequestParam int requestingUserId,
            @PathVariable int targetUserId) {

        if (!channelService.promoteUserToAdmin(channelId, requestingUserId, targetUserId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("Only the channel owner can promote users.")
                    .build();
        }

        return AppResponse.success()
                .withMessage("User promoted to Admin successfully.")
                .build();
    }

    @PutMapping("/channels/{channelId}/rename")
    public ResponseEntity<?> renameChannel(
            @PathVariable int channelId,
            @RequestParam int userId,
            @RequestParam String newName) {

        if (!channelService.renameChannel(channelId, userId, newName)) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("Only Admins or Owners can rename the channel.")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Channel renamed successfully.")
                .build();
    }

}
