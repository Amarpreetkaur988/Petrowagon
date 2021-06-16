package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.RejectedOrderModel;
import com.omninos.petrowagon.R;

import java.util.List;

public class RejectedOrderAdapter extends RecyclerView.Adapter<RejectedOrderAdapter.MyViewHolder> {
    List<RejectedOrderModel.Detail> list;
    Context context;


    public RejectedOrderAdapter(List<RejectedOrderModel.Detail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rejected_order, parent, false);
        MyViewHolder  viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.rejectTitle.setText(list.get(position).getTitle());
        holder.rejectQuantity.setText("("+list.get(position).getQuantity()+")");
        holder.rejectLocation.setText(list.get(position).getLocation());
        holder.rejectDate.setText(list.get(position).getDate());
        holder.rejectPrice.setText(list.get(position).getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView rejectPrice, rejectDate, rejectLocation, rejectQuantity, rejectTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rejectPrice = itemView.findViewById(R.id.rejectPrice);
            rejectDate = itemView.findViewById(R.id.rejectDate);
            rejectLocation = itemView.findViewById(R.id.rejectLocation);
            rejectQuantity = itemView.findViewById(R.id.rejectQuantity);
            rejectTitle = itemView.findViewById(R.id.rejectTitle);

        }
    }
}
