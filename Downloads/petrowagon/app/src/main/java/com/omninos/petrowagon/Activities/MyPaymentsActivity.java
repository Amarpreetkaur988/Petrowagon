package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.petrowagon.Adapters.AdapterMyPayments;
import com.omninos.petrowagon.ModelClass.TransactionModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

public class MyPaymentsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AdapterMyPayments adapterMyPayments;
    private TextView tv_head;
    private ImageView img_back;
    private OrderViewModel viewModel;
    private Activity activity;
    private TextView transactionText,proNameTransaction, proQuantityTransaction, proLocationTransaction, proDateTransaction,proTotalPriceTransaction;
    private LayoutInflater layoutInflater;
    private Button BtnBackTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payments);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        initIds();
        tv_head.setText("TRANSACTIONS");
        getList();
    }

    private void initIds() {
        activity = MyPaymentsActivity.this;
        recyclerView=findViewById(R.id.recycler_my_payments);
        tv_head=findViewById(R.id.tv_head_topbar);
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        transactionText = findViewById(R.id.transactionText);

    }

    private void getList(){
        String userId= App.getAppPreference().getLoginDetail().getDetails().getId();

        viewModel.transactionList(activity, userId).observe(MyPaymentsActivity.this, new Observer<TransactionModel>() {
            @Override
            public void onChanged(TransactionModel transactionModel) {
                if (transactionModel.getSuccess().equalsIgnoreCase("1")){
                    transactionText.setVisibility(View.GONE);
                    adapterMyPayments=new AdapterMyPayments(transactionModel.getDetails(), MyPaymentsActivity.this, new AdapterMyPayments.Select() {
                        @Override
                        public void Choose(int position) {
                            App.getSinltonPojo().setProductName(transactionModel.getDetails().get(position).getTitle());
                            App.getSinltonPojo().setProductQuantity(transactionModel.getDetails().get(position).getQuantity());
                            App.getSinltonPojo().setProductLocation(transactionModel.getDetails().get(position).getLocation());
                            App.getSinltonPojo().setProductDate(transactionModel.getDetails().get(position).getCreated());
                            App.getSinltonPojo().setProductTotalPrice(transactionModel.getDetails().get(position).getTotalPrice());
                            detailDialog();
                        }
                    });
                    recyclerView.setAdapter(adapterMyPayments);

                }
                else {
//                    Toast.makeText(activity, transactionModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void detailDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.transaction_detail_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(MyPaymentsActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setView(confirmdailog);

        proNameTransaction = confirmdailog.findViewById(R.id.proNameTransaction);
        proQuantityTransaction = confirmdailog.findViewById(R.id.proQuantityTransaction);
        proLocationTransaction = confirmdailog.findViewById(R.id.proLocationTransaction);
        proDateTransaction = confirmdailog.findViewById(R.id.proDateTransaction);
        proTotalPriceTransaction = confirmdailog.findViewById(R.id.proTotalPriceTransaction);

        proNameTransaction.setText(App.getSinltonPojo().getProductName());
        proQuantityTransaction.setText(App.getSinltonPojo().getProductQuantity());
        proLocationTransaction.setText(App.getSinltonPojo().getProductLocation());
        proDateTransaction.setText(App.getSinltonPojo().getProductDate());
        proTotalPriceTransaction.setText(App.getSinltonPojo().getProductTotalPrice());

        BtnBackTransaction = confirmdailog.findViewById(R.id.BtnBackTransaction);
        BtnBackTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnBackTransaction.setBackgroundResource(R.drawable.black_bg);
                BtnBackTransaction.setTextColor(getResources().getColor(R.color.white));
                dailogbox.dismiss();
            }
        });


        dailogbox.show();

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
