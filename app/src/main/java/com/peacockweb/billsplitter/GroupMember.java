package com.peacockweb.billsplitter;

import java.io.Serializable;

/**
 * Created by Andrew on 5/4/2016.
 */
public class GroupMember implements Serializable {
    private String name;
    private String username;
    private String userId;

    public GroupMember(String name, String username, String userId) {
        this.name = name;
        this.username = username;
        this.userId = userId;
    }

    public GroupMember(String n) { username = n; }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getUserId() { return userId; }

    @Override
    public String toString() { return name; }
}
