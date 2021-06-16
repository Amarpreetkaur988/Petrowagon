package com.omninos.petrowagon;

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

import com.omninos.petrowagon.Adapters.RejectedOrderAdapter;
import com.omninos.petrowagon.ModelClass.RejectedOrderModel;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

public class RejectedOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycler_reject_orders;
    private TextView rejectOrderText;
    private OrderViewModel viewModel;
    private Activity activity;
    private RejectedOrderAdapter rejectedOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_order);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        findIds();
        getRejectedrder();
    }

    private void findIds() {
        ImageView back = findViewById(R.id.img_back);
        back.setOnClickListener(this);
        TextView heading = findViewById(R.id.tv_head_topbar);
        heading.setText("Cancelled Orders");
        activity = RejectedOrderActivity.this;
        recycler_reject_orders = findViewById(R.id.recycler_reject_orders);
        rejectOrderText = findViewById(R.id.rejectOrderText);
        recycler_reject_orders.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void getRejectedrder() {
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();

        viewModel.rejectedOrder(activity, userId).observe(RejectedOrderActivity.this, new Observer<RejectedOrderModel>() {
            @Override
            public void onChanged(RejectedOrderModel rejectedOrderModel) {
                if (rejectedOrderModel.getSuccess().equalsIgnoreCase("1")){
                    rejectOrderText.setVisibility(View.GONE);
                    recycler_reject_orders.setAdapter(new RejectedOrderAdapter(rejectedOrderModel.getDetails(), RejectedOrderActivity.this));
                }
                else {
                    rejectOrderText.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
