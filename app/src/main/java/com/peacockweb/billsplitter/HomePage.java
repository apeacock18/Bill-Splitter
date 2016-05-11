package com.peacockweb.billsplitter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    ArrayList<PaymentSummary> paymentSummaries = new ArrayList();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SummaryFragment(), "Summary");
        adapter.addFragment(new SummaryFragment(), "Debts");
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

/*      paymentSummaries.add(new PaymentSummary("Chris", "Pizza", "4/5/16 9:23 PM", new String[]{"Peter", "David"}, 23.87, false));
        paymentSummaries.add(new PaymentSummary("David", "Gas", "4/5/16 9:23 PM", new String[]{"Sarah", "Chris"}, 27.12, false));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));

        PaymentsListAdapter adapter = new PaymentsListAdapter(this, paymentSummaries);
        ListView listView = (ListView) findViewById(R.id.main_bill_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mMessageClickedHandler);*/
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

        }
    };

    public void addBill(View view) {
        Intent intent = new Intent(this, AddBill.class);
        startActivity(intent);
    }

    public void onAddGroupClick(View view) {
        Intent intent = new Intent(view.getContext(), AddGroup.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.friends) {
            Intent intent = new Intent(this, Friends.class);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
