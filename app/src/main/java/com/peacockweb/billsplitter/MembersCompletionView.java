package com.peacockweb.billsplitter;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by Andrew on 5/4/2016.
 */
public class MembersCompletionView extends TokenCompleteTextView<GroupMember> {

    public MembersCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(GroupMember member) {

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.members_token, (ViewGroup)MembersCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(member.getName());

        return view;
    }

    @Override
    protected GroupMember defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new GroupMember(completionText);
        } else {
            return new GroupMember(completionText);
        }
    }
}