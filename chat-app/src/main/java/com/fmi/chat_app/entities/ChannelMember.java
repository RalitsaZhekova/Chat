package com.fmi.chat_app.entities;

public class ChannelMember {

    public static final String TABLE_NAME = "tc_channel_members";

    public static class columns {
        public static final String ID = "id";
        public static final String CHANNEL_ID = "channel_id";
        public static final String USER_ID = "user_id";
        public static final String ROLE = "role";
        public static final String IS_ACTIVE = "is_active";
    }

    private int id;
    private int channelId;
    private int userId;
    private String role;
    private int isActive;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getChannelId() { return channelId; }
    public void setChannelId(int channelId) { this.channelId = channelId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

}
