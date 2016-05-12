package com.peacockweb.billsplitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class FriendFragment extends Fragment {

    String[] myStringArray = {"Friend 1", "Friend 2", "Friend 3"};
    private View view;

    public FriendFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, myStringArray);
        ListView listView1 = (ListView) view.findViewById(R.id.friendsList);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(mMessageClickedHandler);
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Snackbar.make(view.findViewById(R.id.groupsList), "This will show a record of all bills with this individual.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.friends_fragment, container, false);
        return view;
    }
}
