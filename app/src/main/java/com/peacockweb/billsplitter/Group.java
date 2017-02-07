package com.peacockweb.billsplitter;

import com.peacockweb.billsplitter.util.VariableManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by apeacock on 4/29/16.
 */
public class Group {
    ArrayList<String> groupMemberIds;
    ArrayList<Status> statuses;
    ArrayList<Transaction> transactions;
    String name;
    String groupId;

    public Group() {
        groupMemberIds = new ArrayList<>();
        statuses = new ArrayList<>();
        transactions = new ArrayList<>();
        name = "";
        groupId = "";
    }

    public Group(Set<String> members, String name, String groupId,
                 ArrayList<Status> statuses, ArrayList<Transaction> transactions) {
        this.groupId = groupId;
        groupMemberIds = new ArrayList<>();
        groupMemberIds.addAll(members);
        this.name = name;
        this.statuses = statuses;
        this.transactions = transactions;
    }

    public Group(ArrayList<String> members, String name, String groupId) {
        this.groupId = groupId;
        groupMemberIds = members;
        this.name = name;
        this.statuses = new ArrayList<>();
        this.transactions = new ArrayList<>();
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


    public String getMemberNames() {
        String str = "";
        for (int i = 0; i < groupMemberIds.size(); i++) {
            str += VariableManager.findUserNameById(groupMemberIds.get(i));
            if (i < groupMemberIds.size() - 1) {
                str += ", ";
            }
        }
        return str;
    }
}
