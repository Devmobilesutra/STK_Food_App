package com.stk.orderingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.Model.ModelDistributerList;
import com.stk.orderingapp.R;

import java.util.List;

/**
 * Created by JARVIS on 28-Mar-18.
 */

public class DistributerListAdapter extends RecyclerView.Adapter<DistributerListAdapter.MyViewHolder> {
    private List<ModelDistributerList> dastributorList;
    Context context;
    private static final String LOG_TAG = "DistributorDataAdapter";
    String location = "";
    CharSequence current_date_old;


    private int permission_count = 1;

    public DistributerListAdapter(Context context, List<ModelDistributerList> dastributorList) {
        this.dastributorList = dastributorList;
        this.context = context;

    }


   /* public interface DistributorDataAdapterlistner {
        void onContactSelected(ModelDistributerList distributorModel);

    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDistributerName = null, tvDistributerAddress = null, tvLastVisitDate = null;

        public MyViewHolder(View view) {
            super(view);

            MyApplication.log("JARVIS", "in MyViewHolder data-->");
            tvDistributerName = (TextView) itemView.findViewById(R.id.tvDistributerName);
            tvDistributerAddress = (TextView) itemView.findViewById(R.id.tvDistributerAddress);
            tvLastVisitDate = (TextView) itemView.findViewById(R.id.tvLastVisitDate);
        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_dist_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MyApplication.log("JARVIS", "in onBindViewHolder data-->");
        final ModelDistributerList model = dastributorList.get(position);


        holder.tvDistributerName.setText(model.getDistributer_name());
        holder.tvDistributerAddress.setText(model.getDist_address());
        holder.tvLastVisitDate.setText(model.getLast_visit());
    }


    @Override
    public int getItemCount() {
        return dastributorList == null ? 0 : dastributorList.size();
    }

}


/*public class DistributerListAdapter extends RecyclerView.Adapter<DistributerListAdapter.MyViewHolder> {

    final List<ModelDistributerList> rowItem;
    Context context = null;

    public DistributerListAdapter(Context context, List<ModelDistributerList> rowItems) {
        MyApplication.log("JARVIS", "in DistributerListAdapter data-->" + rowItems.toString());
        this.rowItem = rowItems;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyViewHolder vh;
        MyApplication.log("JARVIS", "in onCreateViewHolder data-->");
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_dist_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ModelDistributerList item = rowItem.get(position);

        holder.tvDistributerName.setText(item.getDistributer_name());
        holder.tvDistributerAddress.setText(item.getDist_address());
        holder.tvLastVisitDate.setText(item.getLast_visit());
    }


    @Override
    public int getItemCount() {
        MyApplication.log("JARVIS", "in getItemCount data-->");
        return rowItem == null ? 0 : rowItem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDistributerName = null, tvDistributerAddress = null, tvLastVisitDate = null;

        public MyViewHolder(View view) {
            super(view);
            MyApplication.log("JARVIS", "in MyViewHolder data-->");
            tvDistributerName = (TextView) itemView.findViewById(R.id.tvDistributerName);
            tvDistributerAddress = (TextView) itemView.findViewById(R.id.tvDistributerAddress);
            tvLastVisitDate = (TextView) itemView.findViewById(R.id.tvLastVisitDate);
        }
    }*/
//}
