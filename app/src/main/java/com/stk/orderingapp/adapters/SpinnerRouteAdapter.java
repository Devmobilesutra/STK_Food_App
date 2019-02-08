package com.stk.orderingapp.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stk.orderingapp.DataBase.TABLE_ROUTE_MASTER;
import com.stk.orderingapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JARVIS on 09-Apr-18.
 */

public class SpinnerRouteAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> hashMapArrayList = null;
    Context context = null;

    public SpinnerRouteAdapter(Context context, ArrayList<HashMap<String, String>> hashMaps) {
        this.context = context;
        this.hashMapArrayList = hashMaps;
    }

    @Override
    public int getCount() {
        return this.hashMapArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.hashMapArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        HashMap<String, String> hm = hashMapArrayList.get(position);

        if (convertView != null) {
            view = convertView;
        } else {
            view = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.inflater_row_route, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.tcRouteName);

        if (hm != null) {
            textView.setText("  " + hm.get(TABLE_ROUTE_MASTER.COL_ROUTE_NAME)+"  " );
        }


        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
