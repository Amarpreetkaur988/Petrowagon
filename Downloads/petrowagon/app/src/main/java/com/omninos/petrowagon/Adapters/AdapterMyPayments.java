package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.TransactionModel;
import com.omninos.petrowagon.R;

import java.util.List;

public class AdapterMyPayments extends RecyclerView.Adapter<AdapterMyPayments.ViewHolder> {
    List<TransactionModel.Detail> list;
    Context context;
    Select select;


    public interface Select{
        void Choose(int position);
    }

    public AdapterMyPayments(List<TransactionModel.Detail> list, Context context, Select select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_my_payments, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.priceTransaction.setText(list.get(position).getTotalPrice());
        holder.serviceTransaction.setText("Paid For "+list.get(position).getTitle());
        holder.timeTransaction.setText(list.get(position).getCreated()+" "+list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceTransaction, serviceTransaction, timeTransaction;
        private CardView transactionDeatil_CV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            priceTransaction = itemView.findViewById(R.id.priceTransaction);
            serviceTransaction = itemView.findViewById(R.id.serviceTransaction);
            timeTransaction = itemView.findViewById(R.id.timeTransaction);
            transactionDeatil_CV = itemView.findViewById(R.id.transactionDeatil_CV);
            transactionDeatil_CV.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.transactionDeatil_CV:
                    select.Choose(getLayoutPosition());
                    break;
            }

        }
    }
}
