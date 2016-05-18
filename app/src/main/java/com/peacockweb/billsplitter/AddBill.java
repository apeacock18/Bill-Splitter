package com.peacockweb.billsplitter;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.TinyDB;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddBill extends AppCompatActivity {

    Button payerText;
    EditText total;
    EditText description;
    Button date;
    TinyDB tinyDB;
    ArrayList<GroupMember> payers;
    ArrayList<String> recipients;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        tinyDB = new TinyDB(this);
        payerText = (Button) findViewById(R.id.mainPayer);
        total = (EditText) findViewById(R.id.billTotal);
        description = (EditText) findViewById(R.id.billDescription);
        date = (Button) findViewById(R.id.dateInput);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        ArrayList groupMembers;
        recipients = new ArrayList<>();
        if (tinyDB.getObject("currentGroup", Group.class) != null) {
            Group currentGroup = (Group) tinyDB.getObject("currentGroup", Group.class);
            groupMembers = currentGroup.groupMembers;
            for (Object mem : groupMembers) {
                recipients.add(((GroupMember)mem).getName());
            }
        }

        payers = new ArrayList<>();
        if (tinyDB.getObject("currentGroup", Group.class) != null) {
            Group currentGroup = (Group) tinyDB.getObject("currentGroup", Group.class);
            payers = currentGroup.groupMembers;
        }

        PayerListAdapter adapter = new PayerListAdapter(this, payers);
        ListView listView1 = (ListView) findViewById(R.id.billPayerList);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);
        listView1.setScrollContainer(false);

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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        date.setText(new StringBuilder().append(month).append("-")
                .append(day).append("-").append(year));
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
            if (fieldsAreValid()) {
                PaymentSummary summary = new PaymentSummary(
                        payerText.getText().toString(),
                        description.getText().toString(),
                        date.getText().toString(),
                        recipients.toArray(new String[recipients.size()]),
                        Double.parseDouble(total.getText().toString()),
                        false
                );

                //if (tinyDB.getListObject("paymentSummaries", PaymentSummary.class) != null) {
                ArrayList paymentSummaries = tinyDB.getListObject("paymentSummaries", PaymentSummary.class);
                paymentSummaries.add(summary);
                tinyDB.putListObject("paymentSummaries", paymentSummaries);

                final Group group = (Group) tinyDB.getObject("currentGroup", Group.class);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.getInBackground(group.groupId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject group, ParseException e) {;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("groupId", group.getObjectId());
                        params.put("payee", SignIn.user.getObjectId());
                        HashMap split = new HashMap();
                        List memberIds = group.getList("members");
                        for (int i = 0; i < memberIds.size(); i++) {
                            split.put(memberIds.get(i), 100 / memberIds.size());
                        }
                        params.put("split", split);
                        params.put("transactionAmount", Double.parseDouble(total.getText().toString()));
                        params.put("description", description.getText().toString());
                        params.put("date", date.getText().toString());
                        ParseCloud.callFunctionInBackground("newTransaction", params, new FunctionCallback<Object>() {
                            public void done(Object id, ParseException e) {
                                if (e == null) {
                                    System.out.println("Transaction was added to parse.");
                                    Intent intent = new Intent(getBaseContext(), HomePage.class);
                                    startActivity(intent);
                                } else {
                                    System.out.println("Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                                //System.out.println("GOT TO HERE!");
                            }
                        });
                    }
                });
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

    public void payerClick(View view) {
        PopupMenu menu = new PopupMenu(this, payerText);
        for (GroupMember payer : payers) {
            menu.getMenu().add(payer.getName());
        }

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                payerText.setText(item.getTitle());
                return false;
            }
        });

        menu.show();
    }

    public void showDatePicker(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }
}
