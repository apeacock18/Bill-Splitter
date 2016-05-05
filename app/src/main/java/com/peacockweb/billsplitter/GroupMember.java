package com.peacockweb.billsplitter;

import java.io.Serializable;

/**
 * Created by Andrew on 5/4/2016.
 */
public class GroupMember implements Serializable {
    private String name;
    private String email;

    public GroupMember(String n, String e) { name = n; email = e; }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return name; }
}
