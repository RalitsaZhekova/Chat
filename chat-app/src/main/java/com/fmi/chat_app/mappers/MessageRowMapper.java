package com.fmi.chat_app.mappers;

import com.fmi.chat_app.entities.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt(Message.columns.ID));
        message.setSenderId(rs.getInt(Message.columns.SENDER_ID));
        message.setChannelId(rs.getObject(Message.columns.CHANNEL_ID) != null ? rs.getInt(Message.columns.CHANNEL_ID) : null);
        message.setReceiverId(rs.getObject(Message.columns.RECEIVER_ID) != null ? rs.getInt(Message.columns.RECEIVER_ID) : null);
        message.setContent(rs.getString(Message.columns.CONTENT));
        message.setIsActive(rs.getInt(Message.columns.IS_ACTIVE));

        return message;
    }

}
