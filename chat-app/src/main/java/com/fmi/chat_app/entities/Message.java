package com.fmi.chat_app.entities;

public class Message {

    public static final String TABLE_NAME = "td_messages";

    public static class columns {
        public static final String ID = "id";
        public static final String SENDER_ID = "sender_id";
        public static final String CHANNEL_ID = "channel_id";
        public static final String RECEIVER_ID = "receiver_id";
        public static final String CONTENT = "content";
        public static final String IS_ACTIVE = "is_active";
    }

    private int id;
    private int senderId;
    private Integer channelId; // Nullable
    private Integer receiverId; // Nullable
    private String content;
    private int isActive;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public Integer getChannelId() { return channelId; }
    public void setChannelId(Integer channelId) { this.channelId = channelId; }

    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

}
