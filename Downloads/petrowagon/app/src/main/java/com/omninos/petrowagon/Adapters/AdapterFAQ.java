package com.omninos.petrowagon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omninos.petrowagon.ModelClass.FAQModel;
import com.omninos.petrowagon.R;

import java.util.List;

public class AdapterFAQ extends RecyclerView.Adapter<AdapterFAQ.MyViewHolder> {
    private List<FAQModel.Detail> list;
    private Context context;

    public AdapterFAQ(List<FAQModel.Detail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.faqQues.setText("Question: "+list.get(position).getQuestion());
        holder.faqAns.setText("Answer: "+list.get(position).getAnswer());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView faqQues, faqAns;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            faqQues = itemView.findViewById(R.id.faqQues);
            faqAns = itemView.findViewById(R.id.faqAns);

        }
    }
}
