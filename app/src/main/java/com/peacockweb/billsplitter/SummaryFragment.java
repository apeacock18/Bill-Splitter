package com.peacockweb.billsplitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.peacockweb.billsplitter.util.TinyDB;

import java.util.ArrayList;


public class SummaryFragment extends Fragment {

    public static ArrayList paymentSummaries;
    public  static PaymentsListAdapter adapter;
    TinyDB tinyDB;
    private View view;

    public SummaryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*paymentSummaries.add(new PaymentSummary("Chris", "Pizza", "4/5/16 9:23 PM", new String[]{"Peter", "David"}, 23.87, false));
        paymentSummaries.add(new PaymentSummary("David", "Gas", "4/5/16 9:23 PM", new String[]{"Sarah", "Chris"}, 27.12, false));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));*/

        /*paymentSummaries = new ArrayList();
        paymentSummaries.add(new PaymentSummary("Chris", "Pizza", "4/5/16 9:23 PM", new String[]{"Peter", "David"}, 23.87, false));
        paymentSummaries.add(new PaymentSummary("David", "Gas", "4/5/16 9:23 PM", new String[]{"Sarah", "Chris"}, 27.12, false));
        paymentSummaries.add(new PaymentSummary("Sarah", "Personal Debt", "4/5/16 9:23 PM", new String[]{"David"}, 13.50, true));*/

        tinyDB= new TinyDB(getContext());
        paymentSummaries = tinyDB.getListObject("paymentSummaries", PaymentSummary.class);

/*        if (tinyDB.getListObject("paymentSummaries", PaymentSummary.class) != null) {
            paymentSummaries = tinyDB.getListObject("paymentSummaries", PaymentSummary.class);
        }*/

        adapter = new PaymentsListAdapter(getContext(), paymentSummaries);
        ListView summaryList = (ListView) view.findViewById(R.id.summary_list);
        summaryList.setAdapter(adapter);
        summaryList.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Snackbar.make(view.findViewById(R.id.summary_list), "This will show a record of all bills with this individual.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_summary, container, false);
        return view;
    }
}

