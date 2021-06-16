package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.CurrentOrderModel;
import com.omninos.petrowagon.R;

import java.util.List;

public class AdapterCurrentOrders extends RecyclerView.Adapter<AdapterCurrentOrders.ViewHolder> {

    List<CurrentOrderModel.Detail> list;
    Context context;
    Select select;

    public interface Select {
        void Choose(int position);
    }


    public AdapterCurrentOrders(List<CurrentOrderModel.Detail> list, Context context, Select select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_current_orders, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productTitleCurrentOrder.setText(list.get(position).getTitle());
        holder.productQuantityCurrentOrder.setText("("+list.get(position).getQuantity()+")");
        holder.productAddressCurrentOrder.setText(list.get(position).getLocation());
        holder.productDateCurrentOrder.setText(list.get(position).getDate());
        holder.productPriceCurrentOrder.setText(list.get(position).getTotalPrice());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productTitleCurrentOrder, productQuantityCurrentOrder, productAddressCurrentOrder, productDateCurrentOrder, productPriceCurrentOrder;
        private LinearLayout productTrackOrderCurrentOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitleCurrentOrder = itemView.findViewById(R.id.productTitleCurrentOrder);
            productQuantityCurrentOrder = itemView.findViewById(R.id.productQuantityCurrentOrder);
            productAddressCurrentOrder = itemView.findViewById(R.id.productAddressCurrentOrder);
            productDateCurrentOrder = itemView.findViewById(R.id.productDateCurrentOrder);
            productPriceCurrentOrder = itemView.findViewById(R.id.productPriceCurrentOrder);
            productTrackOrderCurrentOrder = itemView.findViewById(R.id.productTrackOrderCurrentOrder);
            productTrackOrderCurrentOrder.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.productTrackOrderCurrentOrder:
                    select.Choose(getLayoutPosition());
                    break;
            }

        }
    }
}
