package com.fmi.chat_app.mappers;

import com.fmi.chat_app.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(User.columns.ID));
        user.setName(rs.getString(User.columns.NAME));
        user.setEmail(rs.getString(User.columns.EMAIL));
        user.setIsActive(rs.getInt(User.columns.IS_ACTIVE));

        return user;
    }

}
