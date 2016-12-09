package com.peacockweb.billsplitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.peacockweb.billsplitter.util.VariableManager;

import java.util.ArrayList;
import java.util.HashMap;

public class DebtsFragment extends Fragment {

    public ArrayList<HashMap<String, Object>> data = new ArrayList<>();
    ListView summaryList;
    public static DebtListAdapter adapter;

    public DebtsFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (VariableManager.selectedGroup != null) {
            Group currentGroup = VariableManager.selectedGroup;
            ArrayList<Status> _statuses = currentGroup.statuses;

            for (Status stat : _statuses) {
                if (stat.id.equals(VariableManager.getUserId())) {
                    data = stat.data;
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
