package com.fmi.chat_app.repositories;

import com.fmi.chat_app.entities.Channel;
import com.fmi.chat_app.entities.ChannelMember;
import com.fmi.chat_app.mappers.ChannelMemberRowMapper;
import com.fmi.chat_app.mappers.ChannelRowMapper;
import com.fmi.chat_app.system.db.QueryBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.tree.RowMapper;
import java.util.List;

@Repository
public class ChannelRepository {

    private final QueryBuilder<Channel> db;
    JdbcTemplate jdbcTemplate;

    public ChannelRepository(QueryBuilder<Channel> db, JdbcTemplate jdbcTemplate) {this.db = db; this.jdbcTemplate = jdbcTemplate;}

    public boolean createChannel(String name, int ownerId, int isPrivate) {

        db.insertInto(Channel.TABLE_NAME)
                .withValue(Channel.columns.NAME, name)
                .withValue(Channel.columns.OWNER_ID, ownerId)
                .withValue(Channel.columns.IS_PRIVATE, isPrivate)
                .insert();

        String sql = "SELECT MAX(id) AS id FROM td_channels".toString();
        int channelId = jdbcTemplate.queryForObject(sql, Integer.class);

        if (channelId > 0) {
            return db.insertInto(ChannelMember.TABLE_NAME)
                    .withValue(ChannelMember.columns.CHANNEL_ID, channelId)
                    .withValue(ChannelMember.columns.USER_ID, ownerId)
                    .withValue(ChannelMember.columns.ROLE, "OWNER")
                    .insert();
        }

        return false;
    }

    public boolean deleteChannel(int channelId, int ownerId) {
        return db.updateTable(Channel.TABLE_NAME)
                .set(Channel.columns.IS_ACTIVE, 0)
                .where(Channel.columns.ID, channelId)
                .andWhere(Channel.columns.OWNER_ID, ownerId)
                .andWhere(Channel.columns.IS_ACTIVE, 1)
                .update() > 0;
    }

    public List<Channel> fetchUserChannels(int userId) {
        return db.select("c.*")
                .from("td_channels c")
                .join("tc_channel_members cm", "c.id = cm.channel_id")
                .where("cm.user_id", userId)
                .andWhere("cm.is_active", 1)
                .andWhere("c.is_active", 1)
                .fetchAll(new ChannelRowMapper());
    }

    public boolean addUserToChannel(int channelId, int userId, String role) {
        return db.insertInto(ChannelMember.TABLE_NAME)
                .withValue(ChannelMember.columns.CHANNEL_ID, channelId)
                .withValue(ChannelMember.columns.USER_ID, userId)
                .withValue(ChannelMember.columns.ROLE, role)
                .insert();
    }

    public boolean removeUserFromChannel(int channelId, int userId) {
        return db.updateTable(ChannelMember.TABLE_NAME)
                .set(ChannelMember.columns.IS_ACTIVE, 0)
                .where(ChannelMember.columns.CHANNEL_ID, channelId)
                .andWhere(ChannelMember.columns.USER_ID, userId)
                .andWhere(ChannelMember.columns.IS_ACTIVE, 1)
                .update() > 0;
    }

    public boolean promoteToAdmin(int channelId, int ownerId, int userId) {
        if (!isOwner(channelId, ownerId)) {
            return false;
        }

        return db.updateTable(ChannelMember.TABLE_NAME)
                .set(ChannelMember.columns.ROLE, "ADMIN")
                .where(ChannelMember.columns.CHANNEL_ID, channelId)
                .andWhere(ChannelMember.columns.USER_ID, userId)
                .andWhere(ChannelMember.columns.IS_ACTIVE, 1)
                .update() > 0;
    }

    public boolean renameChannel(int channelId, int userId, String newName) {
        if (!isOwnerOrAdmin(channelId, userId)) {
            return false;
        }

        return db.updateTable(Channel.TABLE_NAME)
                .set(Channel.columns.NAME, newName)
                .where(Channel.columns.ID, channelId)
                .andWhere(Channel.columns.IS_ACTIVE, 1)
                .update() > 0;
    }

    public boolean isOwnerOrAdmin(int channelId, int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM tc_channel_members
        WHERE channel_id = ? AND user_id = ? AND is_active = 1
          AND (role = 'OWNER' OR role = 'ADMIN')
    """.toString();

        int count = jdbcTemplate.queryForObject(sql, Integer.class, channelId, userId);
        return count > 0;
    }

    public boolean isOwner(int channelId, int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM tc_channel_members
        WHERE channel_id = ? AND user_id = ? AND is_active = 1
          AND role = 'OWNER'
    """.toString();

        int count = jdbcTemplate.queryForObject(sql, Integer.class, channelId, userId);
        return count > 0;
    }

    public boolean isUserInChannel(int channelId, int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM tc_channel_members
        WHERE channel_id = ? AND user_id = ? AND is_active = 1
    """.toString();

        int count = jdbcTemplate.queryForObject(sql, Integer.class, channelId, userId);
        return count > 0;
    }

}
