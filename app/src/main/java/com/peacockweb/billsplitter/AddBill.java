package com.peacockweb.billsplitter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListPopupWindow;

import java.util.ArrayList;
import java.util.List;

public class AddBill extends AppCompatActivity
implements AdapterView.OnItemClickListener {


    String[] friends = {"John Peterson", "Chris Baines", "Test","Andrew Peacock", "Davis Mariotti"};
    ArrayList<String> addedFriends;
    AutoCompleteTextView billMembers;
    ListPopupWindow friendsPopup;
    GroupMember[] people;
    ArrayAdapter<GroupMember> adapter;

    MembersCompletionView completionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        billMembers = (AutoCompleteTextView) findViewById(R.id.billAddPersonText);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, friends);
        billMembers.setAdapter(adapter);

        friendsPopup.setOnItemClickListener(
                this);
        billMembers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                AddFriendsDialog newFragment = new AddFriendsDialog();
                newFragment.show(ft, "dialog");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        for (int i = 0; i < addedFriends.size(); i++) {
            billMembers.append(addedFriends.get(i));
            if (i != addedFriends.size() - 1) {
                billMembers.append(", ");
            }
        }
        friendsPopup.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_actions, menu);
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
/*            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);*/
            Snackbar.make(findViewById(R.id.editText), "Successfully created bill!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
