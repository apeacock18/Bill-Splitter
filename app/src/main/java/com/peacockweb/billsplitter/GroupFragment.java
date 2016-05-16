package com.peacockweb.billsplitter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peacockweb.billsplitter.util.TinyDB;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    public static ArrayList groupsData;
    public static GroupListAdapter groupAdapter;
    TinyDB tinyDB;
    private View view;

    public GroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        tinyDB = new TinyDB(getContext());
        groupsData = tinyDB.getListObject("groupList", Group.class);
        groupAdapter = new GroupListAdapter(getContext(), groupsData);
        Button currentGroup = (Button) getActivity().findViewById(R.id.currentGroupButton);
        if (tinyDB.getObject("currentGroup", Group.class) != null) {
            Group group = (Group) tinyDB.getObject("currentGroup", Group.class);
            currentGroup.setText(group.name);
        }
        else if (!groupsData.isEmpty()) {
            tinyDB.putObject("currentGroup", groupsData.get(0));
            currentGroup.setText(((Group)groupsData.get(0)).name);
        }

        ListView listView1 = (ListView) view.findViewById(R.id.groupsList);
        listView1.setAdapter(groupAdapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Button currentGroup = (Button) getActivity().findViewById(R.id.currentGroupButton);
            Group group = (Group) groupsData.get(position);
            currentGroup.setText(group.name);
            tinyDB.putObject("currentGroup", group);
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
