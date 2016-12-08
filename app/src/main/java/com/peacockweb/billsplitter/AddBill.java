package com.peacockweb.billsplitter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.peacockweb.billsplitter.util.VariableManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddBill extends AppCompatActivity {

    Button payerText;
    EditText total;
    EditText description;
    Button date;
    //TinyDB tinyDB;
    List<Integer> payers;
    List<String> recipients;
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

        payerText = (Button) findViewById(R.id.mainPayer);
        total = (EditText) findViewById(R.id.billTotal);
        description = (EditText) findViewById(R.id.billDescription);
        date = (Button) findViewById(R.id.dateInput);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        total.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals(current)){
                    total.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    total.setText(formatted);
                    total.setSelection(formatted.length());

                    total.addTextChangedListener(this);
                }
            }
        });

        List groupMembers;
        recipients = new ArrayList<>();
        if (VariableManager.selectedGroup != null) {
            Group currentGroup = VariableManager.selectedGroup;
            groupMembers = currentGroup.groupMembers;
            for (Object mem : groupMembers) {
                recipients.add(((GroupMember)mem).getName());
            }
        }

        payers = new ArrayList<>();
        if (VariableManager.selectedGroup != null) {
            Group currentGroup = VariableManager.selectedGroup;
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

                System.out.println("Name: " + payerText.getText().toString());
                //System.out.println("ObjectID: " + VariableManager.getObjectIDFromName(payerText.getText().toString()));
                final Group group = VariableManager.selectedGroup;
                final Context context = this;
                /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                query.getInBackground(group.groupId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject group, ParseException e) {;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("groupId", VariableManager.currentGroup.groupId);
                        params.put("payee", VariableManager.getObjectIDFromName(payerText.getText().toString()));
                        HashMap split = new HashMap();
                        List memberIds = group.getList("members");
                        for (int i = 0; i < memberIds.size(); i++) {
                            double num = 1 / new Double(memberIds.size());
                            split.put(memberIds.get(i), num);
                        }
                        params.put("split", split);
                        try {
                            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                            Number number = format.parse(total.getText().toString().substring(1));
                            params.put("amount", Double.parseDouble(total.getText().toString().substring(1).replaceAll("[,]", "")));
                            System.out.println(Double.parseDouble(total.getText().toString().substring(1).replaceAll("[,]", "")));
                        }
                        catch (java.text.ParseException ex) {
                            ex.printStackTrace();
                        }
                        params.put("description", description.getText().toString());
                        params.put("date", date.getText().toString());
                        ParseCloud.callFunctionInBackground("newTransaction", params, new FunctionCallback<Object>() {
                            public void done(Object id, ParseException e) {
                                if (e == null) {
                                    System.out.println("Transaction was added to parse.");
                                    VariableManager.currentGroup.updateStatuses();
                                    VariableManager.currentGroup.updateTransactions(context);
                                } else {
                                    System.out.println("Error: " + e.getMessage());
                                    e.printStackTrace();
                                }
                                //System.out.println("GOT TO HERE!");
                            }
                        });
                    }
                });*/
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
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        PopupMenu menu = new PopupMenu(this, payerText);
        // TODO
        for (Integer payer : payers) {
            menu.getMenu().add(payer);
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
