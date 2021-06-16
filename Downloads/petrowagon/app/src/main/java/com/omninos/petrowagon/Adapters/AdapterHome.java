package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.ProductListModel;
import com.omninos.petrowagon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {

    List<ProductListModel.Detail> list;
    Context context;
    static Select select;

    public AdapterHome(List<ProductListModel.Detail> list, Context context,Select select) {
        this.list = list;
        this.context = context;
        this.select=select;
    }

    public interface Select {
        void Choose(int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_pro_name.setText(list.get(position).getTitle());
//        if (list.get(position).getType()!=""){
//            holder.tv_pro_type.setText(list.get(position).getType());
//            holder.tv_pro_type.setVisibility(View.VISIBLE);
//        }else {
//            holder.tv_pro_type.setVisibility(View.GONE);
//        }

        holder.tv_pro_price.setText(list.get(position).getPrice()+" per litre");
        Picasso.get().load("http://petrowagon.com/petrowagonApplication/"+list.get(position).getImage().toString()).into(holder.Iv_pro_img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_pro_name,tv_pro_price,tv_pro_type;
        private CardView card_pro;
        private ImageView Iv_pro_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_pro_name=itemView.findViewById(R.id.tv_pro_name);
            tv_pro_price=itemView.findViewById(R.id.tv_pro_price);
            card_pro=itemView.findViewById(R.id.card_pro);
//            tv_pro_type=itemView.findViewById(R.id.tv_pro_type);
            Iv_pro_img = itemView.findViewById(R.id.Iv_pro_img);

            card_pro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.Choose(getLayoutPosition());
                }
            });
        }
    }
}
