package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.DescriptionModel;
import com.omninos.petrowagon.R;

import java.util.List;

public class AdapterHomeDescription extends RecyclerView.Adapter<AdapterHomeDescription.MyViewHolder> {
    private List<DescriptionModel.Detail> list;
    private Context context;

    public AdapterHomeDescription(List<DescriptionModel.Detail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_layout, parent, false);
       MyViewHolder viewHolder = new MyViewHolder(view);
       return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.productTitleDialog.setText(list.get(position).getTitle());
        holder.productDescriptionDialog.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView productTitleDialog, productDescriptionDialog;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productDescriptionDialog = itemView.findViewById(R.id.productDescriptionDialog);
            productTitleDialog = itemView.findViewById(R.id.productTitleDialog);
        }
    }
}
