package com.fmi.chat_app.mappers;

import com.fmi.chat_app.entities.Friend;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRowMapper implements RowMapper<Friend> {

    @Override
    public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friend friend = new Friend();
        friend.setId(rs.getInt(Friend.columns.ID));
        friend.setUserId1(rs.getInt(Friend.columns.USER_ID_1));
        friend.setUserId2(rs.getInt(Friend.columns.USER_ID_2));
        friend.setIsActive(rs.getInt(Friend.columns.IS_ACTIVE));

        return friend;
    }

}
