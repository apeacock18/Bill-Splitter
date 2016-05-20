package com.peacockweb.billsplitter;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by apeacock on 5/19/16.
 */
public class VariableManager {
    private VariableManager() {}

    public static ArrayList<Group> groups = new ArrayList<>();
    public static ArrayList<GroupMember> users = new ArrayList<>();

    public static String userId = "";
    public static String username = "";
    public static String name = "";

    public static ParseObject user;

    public static Group currentGroup;

    public static String getNameFromID(String userID) {
        for (GroupMember user : users) {
            if (userID.equals(user.getUserId())) {
                return user.getName();
            }
        }
        return null;
    }

    // TODO Remove this...
    // I shouldn't be treating names as unique ids
    public static String getObjectIDFromName(String name) {
        for (GroupMember user : users) {
            if (name.equals(user.getName())) {
                return user.getUserId();
            }
        }
        return null;
    }
}
