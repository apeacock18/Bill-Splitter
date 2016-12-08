package com.peacockweb.billsplitter;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apeacock on 4/29/16.
 */
public class Group {
    ArrayList<Integer> groupMembers;
    ArrayList<Status> statuses;
    ArrayList<Transaction> transactions;
    String name;
    String groupId;

    public Group() {
        groupMembers = new ArrayList<>();
        statuses = new ArrayList<>();
        transactions = new ArrayList<>();
        name = "";
        groupId = "";
    }

    public Group(ArrayList<Integer> members, String name, String groupId,
                 ArrayList<Status> statuses, ArrayList<Transaction> transactions) {
        this.groupId = groupId;
        groupMembers = members;
        this.name = name;
        this.statuses = statuses;
        this.transactions = transactions;
    }

    public Group(ArrayList<Integer> members, String name, String groupId) {
        this.groupId = groupId;
        groupMembers = members;
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
}
