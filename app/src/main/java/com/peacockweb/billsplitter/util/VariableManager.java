package com.peacockweb.billsplitter.util;

import com.peacockweb.billsplitter.Group;
import com.peacockweb.billsplitter.Transaction;
import com.peacockweb.billsplitter.User;

import java.util.ArrayList;

/**
 * Created by apeacock on 12/7/16.
 */
public class VariableManager {

    private static String username;
    private static String userID;
    private static String fName;
    private static String lName;
    private static String email;
    private static String phoneNumber;
    private static String token;
    public static Group selectedGroup;
    public static ArrayList<Group> groups;
    public static ArrayList<User> users;


    public static void Init(String username, String fName, String lName, String email,
                       String token, String phoneNumber, String userID) {

        users = new ArrayList<>();
        groups = new ArrayList<>();

        VariableManager.username = username;
        VariableManager.userID = userID;
        VariableManager.fName = fName;
        VariableManager.lName = lName;
        VariableManager.email = email;
        VariableManager.token = token;
        VariableManager.phoneNumber = phoneNumber;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void addGroup(Group group) {
        groups.add(group);
    }

    public static String getUsername() {
        return username;
    }

    public static String getUserId() {
        return userID;
    }

    public static String getFullName() {
        return fName + " " + lName;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static String getToken() {
        return token;
    }
}
