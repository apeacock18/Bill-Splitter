package com.peacockweb.billsplitter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.peacockweb.billsplitter.util.NetworkManager;
import com.peacockweb.billsplitter.util.VariableManager;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroup extends AppCompatActivity implements AddGroupMemberDialog.NoticeDialogListener, TokenCompleteTextView.TokenListener {

    EditText groupName;
    ArrayList<GroupMember> people;
    ArrayList<GroupMember> addedMembers = new ArrayList();
    ArrayAdapter<GroupMember> adapter;
    MembersCompletionView completionView;
    AddGroupMemberDialog dialog;
    FragmentManager manager;
    //TinyDB tinyDB;
    String groupId;
    ArrayList<String> usernames = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dialog = new AddGroupMemberDialog();
        manager = getSupportFragmentManager();
        groupName = (EditText) findViewById(R.id.newGroupName);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, people);

        completionView = (MembersCompletionView)findViewById(R.id.addGroupMembers);
        completionView.setTokenListener(this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        completionView.setDeletionStyle(TokenCompleteTextView.TokenDeleteStyle.ToString);
        completionView.setAdapter(adapter);
    }

    @Override
    public void onTokenAdded(Object token) {
        GroupMember mem = (GroupMember) token;
        addedMembers.add(mem);
        System.out.println("Added: " + mem.getUsername());
    }

    @Override
    public void onTokenRemoved(Object token) {
        addedMembers.remove(token);
        System.out.println("Removed: " + token);
    }

    public void onCreateGroupClick(View view) {
        String str = groupName.getText().toString();
        if (str != null && !str.isEmpty()) {

            ArrayList<String> ids = new ArrayList<>();
            for (GroupMember groupMember : addedMembers) {
                usernames.add(groupMember.getUsername());
                ids.add(groupMember.getUserId());
            }
            NetworkManager.createGroup(str, ids);
            finish();

            /*ParseCloud.callFunctionInBackground("createGroup", params, new FunctionCallback<String>() {
                public void done(String id, ParseException e) {
                    if (e == null) {

                        groupId = id;
                        usernames = new ArrayList();

                        for (GroupMember groupMember : addedMembers) {
                            usernames.add(groupMember.getUsername());
                        }
                        if (!usernames.contains(VariableManager.username)) {
                            usernames.add(VariableManager.username);
                        }

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
                        query.whereContainedIn("username", usernames);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> users, ParseException e) {
                                if (e == null) {
                                    if (!users.isEmpty()) {

                                        addGroupMembers(users, 0, new addGroupMemberCalllback() {
                                            @Override
                                            public void groupMemberAddedComplete(Exception e) {
                                                if (e == null) {
                                                    Group group = new Group(addedMembers, groupName.getText().toString(), groupId);
                                                    VariableManager.groups.add(group);
                                                    ManageGroups.groupAdapter.notifyDataSetChanged();
                                                    finish();
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    System.out.println("Error: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        e.printStackTrace();
                    }
                }
            });*/
        }
    }

    @Override
    public void onDialogPositiveClick(String username, String firstName, String lastName, String id) {

        /*arseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null) {
                    if (!users.isEmpty()) {
                        ParseObject user = users.get(0);
                        GroupMember mem = new GroupMember(user.getString("name"), user.getString("username"), user.getObjectId());
                        completionView.addObject(mem);
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });*/
        GroupMember mem = new GroupMember(firstName, lastName, username, id);
        completionView.addObject(mem);
    }

    public void addMemberClick(View view) {
        dialog.show(manager, "addGroupMember");
    }

    public interface addGroupMemberCallback {
        void groupMemberAddedComplete(Exception e);
    }

    public void addGroupMembers(final List members, final int index, final addGroupMemberCallback callback) {
        if (index >= members.size()) {
            if (callback != null) {
                callback.groupMemberAddedComplete(null);
            }
        }
        else {
            /*ParseObject mem = (ParseObject) members.get(index);
            HashMap<String, String> params1 = new HashMap<>();
            params1.put("userId", mem.getObjectId());
            params1.put("groupId", groupId);
            ParseCloud.callFunctionInBackground("addUserToGroup", params1, new FunctionCallback<Object>() {
                public void done(Object id, ParseException e) {
                    if (e == null) {
                        addGroupMembers(members, index + 1, callback);
                        System.out.println("User should have been added here...");
                    } else {
                        callback.groupMemberAddedComplete(e);
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            });*/
        }
    }
}
