package com.fmi.chat_app.repositories;

import com.fmi.chat_app.entities.Message;
import com.fmi.chat_app.entities.User;
import com.fmi.chat_app.mappers.MessageRowMapper;
import com.fmi.chat_app.system.db.QueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {

    private final QueryBuilder<Message> db;

    public MessageRepository(QueryBuilder<Message> db) {
        this.db = db;
    }

    public List<Message> fetchChannelMessages(int channelId) {
        return db.select("m.*, u.name AS sender_name")
                .from(Message.TABLE_NAME + " m")
                .join(User.TABLE_NAME +" u", "m.sender_id = u.id")
                .where("m." + Message.columns.CHANNEL_ID, channelId)
                .andWhere("m." + Message.columns.IS_ACTIVE, 1)
                .orderBy("m." + Message.columns.ID, "ASC")
                .fetchAll(new MessageRowMapper());
    }

    public List<Message> fetchFriendMessages(int userId, int friendId) {
        return db.select("m.*, u.name AS sender_name")
                .from(Message.TABLE_NAME + " m")
                .join(User.TABLE_NAME +" u", "m.sender_id = u.id")
                .rawWhere("(m.sender_id = ? AND m.receiver_id = ?) OR (m.sender_id = ? AND m.receiver_id = ?)",
                        userId, friendId, friendId, userId)
                .andWhere("m." + Message.columns.IS_ACTIVE, 1)
                .orderBy("m." + Message.columns.ID, "ASC")
                .fetchAll(new MessageRowMapper());
    }

    public boolean sendMessageToChannel(int senderId, int channelId, String content) {
        return db.insertInto(Message.TABLE_NAME)
                .withValue(Message.columns.SENDER_ID, senderId)
                .withValue(Message.columns.CHANNEL_ID, channelId)
                .withValue(Message.columns.CONTENT, content)
                .insert();
    }

    public boolean sendMessageToFriend(int senderId, int receiverId, String content) {
        return db.insertInto(Message.TABLE_NAME)
                .withValue(Message.columns.SENDER_ID, senderId)
                .withValue(Message.columns.RECEIVER_ID, receiverId)
                .withValue(Message.columns.CONTENT, content)
                .insert();
    }

}
