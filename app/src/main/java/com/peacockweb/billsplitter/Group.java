package com.peacockweb.billsplitter;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apeacock on 4/29/16.
 */
public class Group {
    ArrayList<GroupMember> groupMembers;
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

    public Group(ArrayList<GroupMember> members, String name, String groupId,
                 ArrayList<Status> statuses, ArrayList<Transaction> transactions) {
        this.groupId = groupId;
        groupMembers = members;
        this.name = name;
        this.statuses = statuses;
        this.transactions = transactions;
    }

    public Group(ArrayList<GroupMember> members, String name, String groupId) {
        this.groupId = groupId;
        groupMembers = members;
        this.name = name;
        this.statuses = new ArrayList<>();
        this.transactions = new ArrayList<>();
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

    public void updateTransactions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.getInBackground(groupId, new GetCallback<ParseObject>() {
            public void done(ParseObject group, ParseException e) {
                if (e == null) {
                    ArrayList<Transaction> transactionObjects = new ArrayList();
                    List<String> parseTransactions = group.getList("transactions");
                    try {
                        for (String json : parseTransactions) {
                            transactionObjects.add(new Transaction(json));
                        }
                        transactions = transactionObjects;
                        SummaryFragment.adapter.notifyDataSetChanged();
                    }
                    catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public void updateStatuses() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.getInBackground(groupId, new GetCallback<ParseObject>() {
            public void done(ParseObject group, ParseException e) {
                if (e == null) {
                    ArrayList<Status> statusObjects = new ArrayList();
                    List<String> parseStatuses = group.getList("status");
                    try {
                        for (String json : parseStatuses) {
                            statusObjects.add(new Status(json));
                        }
                        statuses = statusObjects;
                        DebtsFragment.adapter.notifyDataSetChanged();
                    }
                    catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}
