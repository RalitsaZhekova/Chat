package com.fmi.chat_app.controllers;

import com.fmi.chat_app.entities.Message;
import com.fmi.chat_app.http.AppResponse;
import com.fmi.chat_app.services.ChannelService;
import com.fmi.chat_app.services.FriendService;
import com.fmi.chat_app.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;
    private final FriendService friendService;

    public MessageController(MessageService messageService, ChannelService channelService, FriendService friendService) {
        this.messageService = messageService;
        this.channelService = channelService;
        this.friendService = friendService;
    }

    @GetMapping("/messages/channel/{channelId}")
    public ResponseEntity<?> getChannelMessages(@PathVariable int channelId) {
        List<Message> messages = messageService.getChannelMessages(channelId);

        if (messages == null) {
            return AppResponse.error()
                    .withCode(HttpStatus.NOT_FOUND)
                    .withMessage("Channel does not exist.")
                    .build();
        }

        return AppResponse.success()
                .withData(messages)
                .build();
    }

    @GetMapping("/messages/private/{userId}/{friendId}")
    public ResponseEntity<?> getFriendMessages(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        List<Message> messages = messageService.getFriendMessages(userId, friendId);

        if (messages == null) {
            return AppResponse.error()
                    .withCode(HttpStatus.NOT_FOUND)
                    .withMessage("Friendship does not exist.")
                    .build();
        }

        return AppResponse.success()
                .withData(messages)
                .build();
    }

    @PostMapping("/messages/channel/send")
    public ResponseEntity<?> sendMessageToChannel(@RequestBody Message messageRequest) {
        int senderId = messageRequest.getSenderId();
        int channelId = messageRequest.getChannelId();
        String content = messageRequest.getContent();

        if (!channelService.isUserInChannel(channelId, senderId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("You are not a member of this channel.")
                    .build();
        }

        if (messageService.sendToChannel(senderId, channelId, content)) {
            return AppResponse.success()
                    .withMessage("Message sent successfully to the channel.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Failed to send message to the channel.")
                .build();
    }

    @PostMapping("/messages/private/send")
    public ResponseEntity<?> sendMessageToFriend(@RequestBody Message messageRequest) {
        int senderId = messageRequest.getSenderId();
        int receiverId = messageRequest.getReceiverId();
        String content = messageRequest.getContent();

        if (!friendService.doesFriendshipExist(senderId, receiverId)) {
            return AppResponse.error()
                    .withCode(HttpStatus.FORBIDDEN)
                    .withMessage("You can only send messages to friends.")
                    .build();
        }

        if (messageService.sendToFriend(senderId, receiverId, content)) {
            return AppResponse.success()
                    .withMessage("Message sent successfully to the friend.")
                    .build();
        }

        return AppResponse.error()
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .withMessage("Failed to send message to the friend.")
                .build();
    }

}
