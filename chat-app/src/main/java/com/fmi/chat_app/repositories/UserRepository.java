package com.fmi.chat_app.repositories;

import com.fmi.chat_app.entities.User;
import com.fmi.chat_app.mappers.UserRowMapper;
import com.fmi.chat_app.system.db.QueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final QueryBuilder<User> db;

    public UserRepository(QueryBuilder<User> queryBuilder) {
        this.db = queryBuilder;
    }

    public boolean create(User user) {
        return db.insertInto(User.TABLE_NAME)
                .withValue(User.columns.NAME, user.getName())
                .withValue(User.columns.EMAIL, user.getEmail())
                .insert();
    }

    public List<User> fetchAll() {
        return db.selectAll()
                .from(User.TABLE_NAME)
                .where(User.columns.IS_ACTIVE, 1)
                .fetchAll(new UserRowMapper());
    }

    public User fetchById(int id) {
        return db.selectAll()
                .from(User.TABLE_NAME)
                .where(User.columns.ID, id)
                .andWhere(User.columns.IS_ACTIVE, 1)
                .fetch(new UserRowMapper());
    }

    public boolean update(User user) {
        return db.updateTable(User.TABLE_NAME)
                .set(User.columns.NAME, user.getName())
                .set(User.columns.EMAIL, user.getEmail())
                .where(User.columns.ID, user.getId())
                .update() > 0;
    }

    public boolean delete(int id) {
        return db.updateTable(User.TABLE_NAME)
                .set(User.columns.IS_ACTIVE, 0)
                .where(User.columns.ID, id)
                .update() > 0;
    }

    public List<User> searchUsers(String query) {
        return db.selectAll()
                .from(User.TABLE_NAME)
                .where(User.columns.NAME, " LIKE ", "%" + query + "%")
                .andWhere(User.columns.IS_ACTIVE, 1)
                .fetchAll(new UserRowMapper());
    }

    public List<User> fetchUserFriends(int userId, int limit, int offset) {
        return db.select("u.*")
                .from(User.TABLE_NAME + " u")
                .rawWhere("u.id IN (SELECT CASE " +
                        "WHEN f.user_id_1 = ? THEN f.user_id_2 " +
                        "WHEN f.user_id_2 = ? THEN f.user_id_1 END " +
                        "FROM td_friends f WHERE f.is_active = 1)", userId, userId)
                .andWhere("u." + User.columns.IS_ACTIVE, 1)
                .limit(limit)
                .offset(offset)
                .fetchAll(new UserRowMapper());
    }

    public List<User> countUserFriends(int userId) {
        return db.select("u.*")
                .from(User.TABLE_NAME + " u")
                .rawWhere("u.id IN (SELECT CASE " +
                        "WHEN f.user_id_1 = ? THEN f.user_id_2 " +
                        "WHEN f.user_id_2 = ? THEN f.user_id_1 END " +
                        "FROM td_friends f WHERE f.is_active = 1)", userId, userId)
                .andWhere("u." + User.columns.IS_ACTIVE, 1)
                .fetchAll(new UserRowMapper());
    }

    public List<User> fetchChannelMembers(int channelId) {
        return db.select("u.*")
                .from("tc_channel_members cm")
                .join("td_users u", "cm.user_id = u.id")
                .where("cm.channel_id", channelId)
                .andWhere("cm.is_active", 1)
                .fetchAll(new UserRowMapper());
    }

}
