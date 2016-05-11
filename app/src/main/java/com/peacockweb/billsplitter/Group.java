package com.peacockweb.billsplitter;

import java.util.ArrayList;

/**
 * Created by apeacock on 4/29/16.
 */
public class Group {
    ArrayList<GroupMember> groupMembers;
    String name;

    public Group(ArrayList<GroupMember> members, String name) {
        groupMembers = members;
        this.name = name;
    }

    public String getMemberNames() {
        String str = "";
        for (int i = 0; i < groupMembers.size(); i++) {
            if (i == 0) {
                str += groupMembers.get(i).getName();
            }
            else {
                str += ", " + groupMembers.get(i).getName();
            }
        }
        return str;
    }
}
