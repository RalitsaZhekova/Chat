package com.fmi.chat_app.mappers;

import com.fmi.chat_app.entities.Channel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelRowMapper implements RowMapper<Channel> {

    @Override
    public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
        Channel channel = new Channel();
        channel.setId(rs.getInt(Channel.columns.ID));
        channel.setName(rs.getString(Channel.columns.NAME));
        channel.setOwnerId(rs.getInt(Channel.columns.OWNER_ID));
        channel.setIsActive(rs.getInt(Channel.columns.IS_ACTIVE));
        channel.setIsPrivate(rs.getInt(Channel.columns.IS_PRIVATE));

        return channel;
    }

}
