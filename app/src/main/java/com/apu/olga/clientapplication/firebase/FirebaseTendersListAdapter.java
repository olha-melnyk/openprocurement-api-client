package com.apu.olga.clientapplication.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.apu.olga.clientapplication.R;
import android.content.Context;
import android.widget.TextView;

import java.util.List;

public class FirebaseTendersListAdapter extends BaseAdapter {

    private android.content.Context mContext;
    private List<Tenders> mTendersList;

    public FirebaseTendersListAdapter(android.content.Context context, List<Tenders> tenderList) {
        this.mContext = context;
        this.mTendersList = tenderList;
    }

    @Override
    public int getCount() {
        return mTendersList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTendersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tenders tenders = mTendersList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_tenders,parent,false);
        }

        TextView userName = (TextView) convertView.findViewById(R.id.textTitle);
        userName.setText(tenders.getTitle());

        return convertView;
    }

    public void update() {
        notifyDataSetChanged();
    }

    public Context getContext() {
        return mContext;
    }
}
