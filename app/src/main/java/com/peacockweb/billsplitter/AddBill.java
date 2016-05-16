package com.peacockweb.billsplitter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;

import com.peacockweb.billsplitter.util.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class AddBill extends AppCompatActivity {

    EditText payerText;
    EditText total;
    EditText description;
    EditText date;
    TinyDB tinyDB;
    ArrayList<GroupMember> payers;
    ArrayList<String> recipients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        tinyDB = new TinyDB(this);

        payerText = (EditText) findViewById(R.id.mainPayer);

        ArrayList groupMembers;
        recipients = new ArrayList<>();
        if (tinyDB.getObject("currentGroup", Group.class) != null) {
            Group currentGroup = (Group) tinyDB.getObject("currentGroup", Group.class);
            groupMembers = currentGroup.groupMembers;
            for (Object mem : groupMembers) {
                recipients.add(((GroupMember)mem).getName());
            }
        }

        total = (EditText) findViewById(R.id.billTotal);
        description = (EditText) findViewById(R.id.billDescription);
        date = (EditText) findViewById(R.id.dateInput);



        payers = new ArrayList<>();
        if (tinyDB.getObject("currentGroup", Group.class) != null) {
            Group currentGroup = (Group) tinyDB.getObject("currentGroup", Group.class);
            payers = currentGroup.groupMembers;
        }

        PayerListAdapter adapter = new PayerListAdapter(this, payers);
        ListView listView1 = (ListView) findViewById(R.id.billPayerList);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);

        int numberOfItems = adapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = adapter.getView(itemPos, null, listView1);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView1.getDividerHeight() *
                (numberOfItems - 1);

        // Set list height.
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView1.setLayoutParams(params);
        listView1.requestLayout();
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

        }
    };

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
            if (fieldsAreValid()) {
                PaymentSummary summary = new PaymentSummary(
                        payerText.getText().toString(),
                        description.getText().toString(),
                        date.getText().toString(),
                        recipients.toArray(new String[recipients.size()]),
                        Double.parseDouble(total.getText().toString()),
                        false
                );

                SummaryFragment.paymentSummaries.add(new PaymentSummary("Chris", "Pizza", "4/5/16 9:23 PM", new String[]{"Peter", "David"}, 23.87, false));
                SummaryFragment.adapter.notifyDataSetChanged();

                //if (tinyDB.getListObject("paymentSummaries", PaymentSummary.class) != null) {
                ArrayList paymentSummaries = tinyDB.getListObject("paymentSummaries", PaymentSummary.class);
                paymentSummaries.add(summary);
                tinyDB.putListObject("paymentSummaries", paymentSummaries);
/*            }
            else {
                ArrayList paymentSummaries = new ArrayList();
                paymentSummaries.add(summary);
                tinyDB.putListObject("paymentSummaries", paymentSummaries);
            }*/
                Intent intent = new Intent(getBaseContext(), HomePage.class);
                startActivity(intent);
            }
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean fieldsAreValid() {
        boolean flag = true;
        if (total.getText().toString().equals(null) || total.getText().toString().equals("")) {
            total.setError("Need enter an amount");
            flag = false;
        }
        if (payerText.getText().toString().equals(null) || payerText.getText().toString().equals("")) {
            payerText.setError("Need to enter a payer");
            flag = false;
        }
        if (description.getText().toString().equals(null) || description.getText().toString().equals("")) {
            description.setError("Need to enter a description");
            flag = false;
        }
        if (date.getText().toString().equals(null) || date.getText().toString().equals("")) {
            date.setError("Need to enter a date");
            flag = false;
        }
        return flag;
    }
}
