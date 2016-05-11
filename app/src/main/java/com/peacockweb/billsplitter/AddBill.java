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

    ArrayList<String> addedFriends;
    AutoCompleteTextView billMembers;
    ListPopupWindow friendsDialog;
    ArrayAdapter<GroupMember> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        billMembers = (AutoCompleteTextView) findViewById(R.id.billAddPersonText);
        billMembers.setAdapter(adapter);

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
        friendsDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite)
        {
            Snackbar.make(findViewById(R.id.editText), "Successfully created bill!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
