package com.example.junaid.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<user> users;
    private ArrayList<ArrayList<message>> messages;

    public MyExpandableListAdapter(Context c, ArrayList<user> d,ArrayList<ArrayList<message>> s) {
        this.context = c;
        this.users = d;
        this.messages = s;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_item, null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvDeptName);
        ImageView ivMore = (ImageView) convertView.findViewById(R.id.ivIndicator);

        user d = users.get(groupPosition);

        tvName.setText(d.uname);

        if (isExpanded) {
            ivMore.setImageResource(R.drawable.collapse);
        }
        else {
            ivMore.setImageResource(R.drawable.expand);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_child_item, null);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        message s = messages.get(groupPosition).get(childPosition);

        tvName.setText(s.msg);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return messages.get(groupPosition).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return messages.get(groupPosition).get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return users.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return users.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
