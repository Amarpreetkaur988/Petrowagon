package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.Adapters.AdapterFAQ;
import com.omninos.petrowagon.ModelClass.FAQModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.ViewModel.DescriptionViewModel;

public class FAQHELPActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_head;
    private ImageView img_back;
    private RecyclerView faqRecyclerView;
    private DescriptionViewModel viewModel;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqhelp);
        viewModel = ViewModelProviders.of(this).get(DescriptionViewModel.class);
        initIds();
        getFAQ();
        tv_head.setText("FAQ/HELP");
    }

    private void initIds() {
        activity = FAQHELPActivity.this;
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void getFAQ() {
        viewModel.faq(activity).observe(FAQHELPActivity.this, new Observer<FAQModel>() {
            @Override
            public void onChanged(FAQModel faqModel) {
                if (faqModel.getSuccess().equalsIgnoreCase("1")) {
                    AdapterFAQ adapterFAQ = new AdapterFAQ(faqModel.getDetails(), FAQHELPActivity.this);
                    faqRecyclerView.setAdapter(adapterFAQ);
                } else {
                    Toast.makeText(activity, faqModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
