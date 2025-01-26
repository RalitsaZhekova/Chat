package com.fmi.chat_app.entities;

public class User {

    public static final String TABLE_NAME = "td_users";

    public static class columns {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String IS_ACTIVE = "is_active";
    }

    private int id;
    private String name;
    private String email;
    private int isActive;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getIsActive() { return isActive; }
    public void setIsActive(int isActive) { this.isActive = isActive; }
}
