package com.fmi.chat_app.mappers;

import com.fmi.chat_app.entities.ChannelMember;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelMemberRowMapper implements RowMapper<ChannelMember> {

    @Override
    public ChannelMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChannelMember channelMember = new ChannelMember();
        channelMember.setId(rs.getInt(ChannelMember.columns.ID));
        channelMember.setChannelId(rs.getInt(ChannelMember.columns.CHANNEL_ID));
        channelMember.setUserId(rs.getInt(ChannelMember.columns.USER_ID));
        channelMember.setRole(rs.getString(ChannelMember.columns.ROLE));
        channelMember.setIsActive(rs.getInt(ChannelMember.columns.IS_ACTIVE));

        return channelMember;
    }
}
