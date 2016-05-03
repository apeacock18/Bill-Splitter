package com.peacockweb.billsplitter;

import java.util.ArrayList;

/**
 * Created by apeacock on 4/29/16.
 */
public class Group {
    ArrayList<String> groupMembers;
    String name;

    public Group() {
        groupMembers = new ArrayList();
        name = "default group name";
    }
}
