package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.CurrentOrderModel;
import com.omninos.petrowagon.R;

import java.util.List;

public class AdapterPastOrders extends RecyclerView.Adapter<AdapterPastOrders.ViewHolder> {

    List<CurrentOrderModel.Detail> list;
    Context context;
    Select select;

    public interface Select {
        void Choose(int position);
    }


    public AdapterPastOrders(List<CurrentOrderModel.Detail> list, Context context, Select select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_past_orders, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productTitlePastOrder.setText(list.get(position).getTitle());
        holder.productQuantityPastOrder.setText("("+list.get(position).getQuantity()+")");
        holder.productAddressPastOrder.setText(list.get(position).getLocation());
        holder.productDatePastOrder.setText(list.get(position).getDate());
        holder.productPricePastOrder.setText(list.get(position).getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productTitlePastOrder, productQuantityPastOrder, productAddressPastOrder, productDatePastOrder, productPricePastOrder;
        private Button productReOrderPastOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productTitlePastOrder = itemView.findViewById(R.id.productTitlePastOrder);
            productQuantityPastOrder = itemView.findViewById(R.id.productQuantityPastOrder);
            productAddressPastOrder = itemView.findViewById(R.id.productAddressPastOrder);
            productDatePastOrder = itemView.findViewById(R.id.productDatePastOrder);
            productPricePastOrder = itemView.findViewById(R.id.productPricePastOrder);
            productReOrderPastOrder = itemView.findViewById(R.id.productReOrderPastOrder);
            productReOrderPastOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.productReOrderPastOrder:
                    select.Choose(getLayoutPosition());
                    break;
            }
        }
    }
}
