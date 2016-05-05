package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.DialogFragment;
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
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroup extends AppCompatActivity implements AddGroupMemberDialog.NoticeDialogListener, TokenCompleteTextView.TokenListener {

    EditText groupName;

    ArrayList<GroupMember> people;
    ArrayAdapter<GroupMember> adapter;
    MembersCompletionView completionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        people = new ArrayList();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < users.size(); i++) {
                        people.add(new GroupMember(
                                users.get(i).getString("fName") + " " + users.get(i).getString("lName"),
                                users.get(i).getString("username")));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        groupName = (EditText) findViewById(R.id.newGroupName);

        adapter = new ArrayAdapter<GroupMember>(this, android.R.layout.simple_list_item_1, people);

        completionView = (MembersCompletionView)findViewById(R.id.addGroupMembers);
        completionView.setTokenListener(this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        completionView.setAdapter(adapter);
    }

    @Override
    public void onTokenAdded(Object token) {
        System.out.println("Added: " + token);
    }

    @Override
    public void onTokenRemoved(Object token) {
        System.out.println("Removed: " + token);
    }

    public void onCreateGroupClick(View view) {
        String str = groupName.getText().toString();
        if (str != null && !str.isEmpty()) {
            HashMap<String, String> params = new HashMap<>();
            params.put("name", groupName.getText().toString());
            ParseCloud.callFunctionInBackground("createGroup", params, new FunctionCallback<String>() {
                public void done(String id, ParseException e) {
                    if (e == null) {
                        finish();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            finish();
        }
    }

    @Override
    public void onDialogPositiveClick(String username) {
        EditText text = (EditText) findViewById(R.id.addGroupMembers);
        text.setText(username);
    }
}
