package com.fmi.chat_app.entities;

public class Channel {

    public static final String TABLE_NAME = "td_channels";

    public static class columns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String OWNER_ID = "owner_id";
        public static final String IS_ACTIVE = "is_active";
        public static final String IS_PRIVATE = "is_private";
    }

    private int id;
    private String name;
    private int ownerId;
    private int isActive;
    private int isPrivate;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

    public int getIsPrivate() { return isPrivate; }
    public void setIsPrivate(int isPrivate) { this.isPrivate = isPrivate; }

}
