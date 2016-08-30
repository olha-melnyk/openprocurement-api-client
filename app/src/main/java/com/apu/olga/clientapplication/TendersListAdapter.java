package com.apu.olga.clientapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apu.olga.clientapplication.model.TenderItem;

import java.util.ArrayList;

public class TendersListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater inflater;
    private ArrayList<TenderItem> tendersItems;

    public TendersListAdapter(Context context, ArrayList<TenderItem> tender) {
        this.ctx = context;
        this.tendersItems = tender;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tendersItems.size();
    }

    @Override
    public Object getItem(int position) {
        return tendersItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.tender_adapter, parent, false);
        }

        TenderItem tender = getTender(position);
        ((TextView) view.findViewById(R.id.textView)).setText(tender.getId());

        return view;
    }

    TenderItem getTender(int position) {
        return ((TenderItem) getItem(position));
    }
}
