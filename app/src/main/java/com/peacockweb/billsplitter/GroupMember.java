package com.peacockweb.billsplitter;

import java.io.Serializable;

/**
 * Created by Andrew on 5/4/2016.
 */
public class GroupMember implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String userId;

    public GroupMember(String firstName, String lastName, String username, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userId = userId;
    }

    public GroupMember(String n) { username = n; }

    public String getName() { return firstName + " " + lastName; }
    public String getUsername() { return username; }
    public String getUserId() { return userId; }

    @Override
    public String toString() { return firstName + " " + lastName; }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof GroupMember))return false;
        GroupMember otherMember = (GroupMember)other;
        if (!otherMember.getUserId().equals(userId)) return false;

        return true;
    }
}
