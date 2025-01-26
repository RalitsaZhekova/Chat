package com.fmi.chat_app.entities;

public class Friend {

    public static final String TABLE_NAME = "td_friends";

    public static class columns {
        public static final String ID = "id";
        public static final String USER_ID_1 = "user_id_1";
        public static final String USER_ID_2 = "user_id_2";
        public static final String IS_ACTIVE = "is_active";
    }

    private int id;
    private int userId1;
    private int userId2;
    private int isActive;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId1() { return userId1; }
    public void setUserId1(int userId1) { this.userId1 = userId1; }

    public int getUserId2() { return userId2; }
    public void setUserId2(int userId2) { this.userId2 = userId2; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }

}
