package com.peacockweb.billsplitter;

import java.util.ArrayList;

/**
 * Created by apeacock on 4/29/16.
 */
public class Group {
    ArrayList<GroupMember> groupMembers;
    String name;
    String groupId;

    public Group(ArrayList<GroupMember> members, String name, String groupId) {
        this.groupId = groupId;
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

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Group)) return false;
        Group otherGroup = (Group) other;
        if (!otherGroup.groupId.equals(groupId)) return false;

        return true;
    }
}
