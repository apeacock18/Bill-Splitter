package com.peacockweb.billsplitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.peacockweb.billsplitter.util.VariableManager;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    public static ArrayList groupsData;
    public static GroupListAdapter groupAdapter;
    private View view;

    public GroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupsData = VariableManager.groups;
        System.out.println(groupsData.size() + " groups contained in adapter");
        Log.d("MyApp", " groups contained in adapter");
        groupAdapter = new GroupListAdapter(getContext(), groupsData);
        Button currentGroup = (Button) getActivity().findViewById(R.id.currentGroupButton);

        if (VariableManager.selectedGroup != null) {
            currentGroup.setText(VariableManager.selectedGroup.name);
        }

        ListView listView1 = (ListView) view.findViewById(R.id.groupsList);
        listView1.setAdapter(groupAdapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Button currentGroup = (Button) getActivity().findViewById(R.id.currentGroupButton);
            Group group = (Group) groupsData.get(position);
            currentGroup.setText(group.name);
            VariableManager.selectedGroup = group;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.group_fragment, container, false);
        return view;
    }
}
