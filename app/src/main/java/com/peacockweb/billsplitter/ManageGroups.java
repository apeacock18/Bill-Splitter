package com.peacockweb.billsplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.peacockweb.billsplitter.util.VariableManager;

import java.util.ArrayList;
import java.util.List;

public class ManageGroups extends AppCompatActivity {

    private Toolbar toolbar;
    public static ArrayList groupsData;
    public static GroupListAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        groupsData = VariableManager.groups;
        System.out.println(groupsData.size() + " groups contained in adapter");
        groupAdapter = new GroupListAdapter(this, groupsData);
        Button currentGroup = (Button) findViewById(R.id.currentGroupButton);
        if (VariableManager.selectedGroup != null) {
            currentGroup.setText(VariableManager.selectedGroup.name);
        }

        ListView listView1 = (ListView) findViewById(R.id.groupsList);
        listView1.setAdapter(groupAdapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Button currentGroup = (Button) findViewById(R.id.currentGroupButton);
            Group group = (Group) groupsData.get(position);
            currentGroup.setText(group.name);
            VariableManager.selectedGroup = group;
            List members = group.groupMembers;
            ArrayList<String> memberIds = new ArrayList<String>();
            for (Object mem : members) {
                if (!VariableManager.getUserId().equals(((GroupMember)mem).getUserId())) {
                    memberIds.add(((GroupMember)mem).getUserId());
                }
            }
            /*ParseObject user = VariableManager.user;
            user.put("currentGroup", group.groupId);
            user.saveInBackground();*/
        }
    };

    public void onAddGroupClick(View view) {
        Intent intent = new Intent(this, AddGroup.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favorite)
        {

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
