package com.peacockweb.billsplitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class SummaryFragment extends Fragment {

    public static ArrayList<Transaction> transactions;
    public  static TransactionsListAdapter adapter;
    private View view;

    public SummaryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (VariableManager.currentGroup != null) {
            transactions = VariableManager.currentGroup.transactions;
        }
        else {
            transactions = new ArrayList<>();
        }

        adapter = new TransactionsListAdapter(getContext(), transactions);
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

