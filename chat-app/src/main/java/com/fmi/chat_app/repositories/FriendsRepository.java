package com.fmi.chat_app.repositories;

import com.fmi.chat_app.entities.Friend;
import com.fmi.chat_app.mappers.FriendRowMapper;
import com.fmi.chat_app.system.db.QueryBuilder;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.tree.RowMapper;

@Repository
public class FriendsRepository {

    private final QueryBuilder<Friend> db;
    private final JdbcTemplate jdbcTemplate;

    public FriendsRepository(QueryBuilder<Friend> queryBuilder, JdbcTemplate jdbcTemplate) {
        this.db = queryBuilder;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addFriend(int userId1, int userId2) {
        return db.insertInto(Friend.TABLE_NAME)
                .withValue(Friend.columns.USER_ID_1, userId1)
                .withValue(Friend.columns.USER_ID_2, userId2)
                .insert();
    }

    public boolean removeFriend(int id) {
        return db.updateTable(Friend.TABLE_NAME)
                .set("is_active", 0)
                .where(Friend.columns.ID, id)
                .update() > 0;
    }

    public boolean doesFriendshipExist(int userId1, int userId2) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
            SELECT *
            FROM td_friends
            WHERE (user_id_1 = ? AND user_id_2 = ?)
               OR (user_id_1 = ? AND user_id_2 = ?)
               AND is_active = 1
            LIMIT 1
        """);

        var friendCollection = jdbcTemplate.query(sql.toString(), new FriendRowMapper(), userId1, userId2, userId2, userId1);
        return !friendCollection.isEmpty();
    }

}
