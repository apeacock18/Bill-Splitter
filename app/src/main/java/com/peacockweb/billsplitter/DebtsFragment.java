package com.peacockweb.billsplitter;

import android.app.Activity;
import android.net.Uri;
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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.TinyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebtsFragment extends Fragment {

    public ArrayList<HashMap<String, Object>> data = new ArrayList<>();
    ListView summaryList;
    public static DebtListAdapter adapter;

    public DebtsFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (VariableManager.currentGroup != null) {
            Group currentGroup = VariableManager.currentGroup;
            ArrayList<Status> _statuses = currentGroup.statuses;
            String id = VariableManager.userId;
            for (Status stat : _statuses) {
                if (stat.id.equals(id)) {
                    data = stat.data;
                    System.out.println("ID: " + data.get(0).get("recipient") + ", Amount: " + data.get(0).get("amount"));
                }
            }
        }
        adapter = new DebtListAdapter(getActivity(), data);
        summaryList = (ListView) getActivity().findViewById(R.id.debt_list);
        summaryList.setAdapter(adapter);
        summaryList.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

        }
    };

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater
    inflater, ViewGroup container, Bundle
    savedInstanceState){
        return inflater.inflate(R.layout.fragment_debts, container, false);
    }
}
