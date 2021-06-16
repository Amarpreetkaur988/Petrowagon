package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.Adapters.AdapterOurPartners;
import com.omninos.petrowagon.ModelClass.PartnerListModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.ViewModel.DescriptionViewModel;

import java.util.ArrayList;
import java.util.List;

public class OurPartnersActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_head;
    private ImageView img_back;
    private RecyclerView recyclerView;
    private AdapterOurPartners adapterOurPartners;
    private List<PartnerListModel.Detail> list=new ArrayList<>();
    private DescriptionViewModel viewModel;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_partners);
        viewModel = ViewModelProviders.of(this).get( DescriptionViewModel.class);
        initIds();
        RecyclerItems();
        partnerList();
        tv_head.setText("OUR PARTNERS");

    }
    private void initIds() {
        activity = OurPartnersActivity.this;
        recyclerView=findViewById(R.id.recycler_our_partners);
        tv_head=findViewById(R.id.tv_head_topbar);
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }
    private void RecyclerItems() {
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }
    private void partnerList(){
        viewModel.partnerList(activity).observe(OurPartnersActivity.this, new Observer<PartnerListModel>() {
            @Override
            public void onChanged(PartnerListModel partnerListModel) {
                if (partnerListModel.getSuccess().equalsIgnoreCase("1")){
                    adapterOurPartners=new AdapterOurPartners(partnerListModel.getDetails(),OurPartnersActivity.this);
                    recyclerView.setAdapter(adapterOurPartners);
                }
                else {
                    Toast.makeText(activity, partnerListModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
