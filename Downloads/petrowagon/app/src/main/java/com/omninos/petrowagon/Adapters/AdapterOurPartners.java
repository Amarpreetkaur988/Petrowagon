package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.PartnerListModel;
import com.omninos.petrowagon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOurPartners extends RecyclerView.Adapter<AdapterOurPartners.ViewHolder> {

    List<PartnerListModel.Detail> list;
    Context context;

    public AdapterOurPartners(List<PartnerListModel.Detail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_our_partners, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load("http://petrowagon.com/petrowagonApplication/"+list.get(position).getImage()).into(holder.imgvw);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgvw;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        imgvw=itemView.findViewById(R.id.img_our_partners);
        }
    }
}
