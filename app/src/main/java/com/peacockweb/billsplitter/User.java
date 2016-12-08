package com.peacockweb.billsplitter;

/**
 * Created by apeacock on 12/7/16.
 */
public class User {
    String username;
    String userID;
    String fName;
    String lName;
    String email;
    String phoneNumber;

    public User() {
        username = "";
        userID = "";
        fName = "";
        lName = "";
        email = "";
        phoneNumber = "";
    }

    public User(String username, String userID, String fName, String lName, String email, String phoneNumber) {
        this.username = username;
        this.userID = userID;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
