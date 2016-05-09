package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.TinyDB;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGroup extends AppCompatActivity implements AddGroupMemberDialog.NoticeDialogListener, TokenCompleteTextView.TokenListener {

    EditText groupName;

    ArrayList<GroupMember> people;
    ArrayList<GroupMember> addedMembers = new ArrayList();
    ArrayList knownMembers;
    ArrayAdapter<GroupMember> adapter;
    MembersCompletionView completionView;
    AddGroupMemberDialog dialog;
    FragmentManager manager;
    TinyDB tinyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        dialog = new AddGroupMemberDialog();
        manager = getSupportFragmentManager();

        tinyDB = new TinyDB(this);
        people = new ArrayList();
        knownMembers = tinyDB.getListObject("knownMembers", GroupMember.class);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < users.size(); i++) {
                        people.add(new GroupMember(
                                users.get(i).getString("fName") + " " + users.get(i).getString("lName"),
                                users.get(i).getString("username"),
                                users.get(i).getObjectId()
                        ));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

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
        System.out.println("Added: " + mem.getUsername());
        addedMembers.add(mem);
    }

    @Override
    public void onTokenRemoved(Object token) {
        System.out.println("Removed: " + token);
        addedMembers.remove(token);
        for (GroupMember mem : addedMembers) {
            System.out.println(mem.getName() + " still here");
        }
    }

    public void onCreateGroupClick(View view) {
        String str = groupName.getText().toString();
        if (str != null && !str.isEmpty()) {
            HashMap<String, String> params = new HashMap<>();
            params.put("name", groupName.getText().toString());
            ParseCloud.callFunctionInBackground("createGroup", params, new FunctionCallback<String>() {
                public void done(String id, ParseException e) {
                    if (e == null) {
                        final String groupId = id;

                        final ArrayList<String> usernames = new ArrayList();
                        for (GroupMember gm : addedMembers) {
                            usernames.add(gm.getUsername());
                        }

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
                        query.whereContainedIn("username", usernames);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> users, ParseException e) {
                                if (e == null) {
                                    for (GroupMember mem : addedMembers)
                                    {
                                        if (!usernames.contains(mem.getUsername())) {
                                            knownMembers.add(mem);
                                        }
                                    }
                                    tinyDB.putListObject("knownMembers", knownMembers);

                                    System.out.println("Made it here...");
                                    for (ParseObject user : users) {
                                        HashMap<String, String> params1 = new HashMap<>();
                                        params1.put("userId", user.getObjectId());
                                        params1.put("groupId", groupId);
                                        ParseCloud.callFunctionInBackground("addUserToGroup", params1, new FunctionCallback<Object>() {
                                            public void done(Object id, ParseException e) {
                                                if (e == null) {
                                                    System.out.println("User should have been added here...");
                                                }
                                                else {
                                                    System.out.println("Error: " + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                    System.out.println("Made it this far...");
                                }
                                else {
                                    System.out.println("ERRRROOOOORRRRRR: " + e.getMessage());
                                }
                            }
                        });


                        finish();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            Group group = new Group(addedMembers, str);
            ArrayList temp = tinyDB.getListObject("groupList", Group.class);
            temp.add(group);
            tinyDB.putListObject("groupList", temp);
            ArrayList arr = tinyDB.getListObject("groupList", Group.class);
            System.out.println(((Group)arr.get(0)).name + " found!");
        }
        else {
            finish();
        }
    }

    @Override
    public void onDialogPositiveClick(String username) {
        for (Object obj : knownMembers) {
            GroupMember mem = (GroupMember) obj;
            String _username = mem.getUsername();
            if (_username.equals(username)) {
                completionView.addObject(new GroupMember(mem.getName(), username, mem.getUserId()));
            }
        }
    }

    public void addMemberClick(View view) {
        dialog.show(manager, "addGroupMember");
    }
}
