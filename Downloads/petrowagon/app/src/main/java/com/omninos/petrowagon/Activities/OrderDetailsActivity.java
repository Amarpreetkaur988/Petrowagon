package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.ChargesModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

import java.text.DecimalFormat;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_proceed;
    private TextView tv_head, productNameOrderDetail, perLitrePriceOrderDetail, DropOrderDateOrderDetail, quantityOrderDetail, totalPriceOderDetail, locationOrderDetail;
    private ImageView img_back;
    private Button unChange[];
    private double total, netTotal, quan, quan1, tempPrice;
    private double slimit, elimit, charges, tempTotal;
    String type;
    private String decimalFormat;
    private OrderViewModel viewModel;
    private Activity activity;
    private String rentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        initIds();
        chargesList();
        //   rateSet();
        tv_head.setText("ORDER DETAILS");

        type = getIntent().getExtras().getString("type");
        if (type.equalsIgnoreCase("payment")) {
            btn_proceed.setText("TRACK");

        } else if (type.equalsIgnoreCase("place_order")) {
            btn_proceed.setText("PROCEED TO PAYMENT");
        }

    }


    private void chargesList() {
        viewModel.chargesList(activity).observe(OrderDetailsActivity.this, new Observer<ChargesModel>() {
            @Override
            public void onChanged(ChargesModel chargesModel) {
                if (chargesModel.getSuccess().equalsIgnoreCase("1")) {
                    for (int i = 0; i < chargesModel.getDetails().size(); i++) {
                        rentType = chargesModel.getDetails().get(i).getType();
                        slimit = Double.parseDouble(chargesModel.getDetails().get(i).getSlimit());
                        elimit = Double.parseDouble(chargesModel.getDetails().get(i).getElimit());
                        charges = Double.parseDouble(chargesModel.getDetails().get(i).getCharges());
                        rateSet();
                    }

                } else {
                    Toast.makeText(activity, chargesModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void rateSet() {
        if (App.getSinltonPojo().getProductStatus() != null) {
            if (App.getSinltonPojo().getProductStatus().equalsIgnoreCase("1")) {
                locationOrderDetail.setText(App.getSinltonPojo().getProductLocation());
                perLitrePriceOrderDetail.setText(App.getSinltonPojo().getProductPrice());
                DropOrderDateOrderDetail.setText(App.getSinltonPojo().getProductDate());
                quantityOrderDetail.setText(App.getSinltonPojo().getProductQuantity());
                productNameOrderDetail.setText(App.getSinltonPojo().getProductName());

                double quantity = Double.parseDouble(App.getSinltonPojo().getProductQuantity());
                double perLitrePrice = Double.parseDouble(App.getSinltonPojo().getProductPrice());
                total = quantity * perLitrePrice;
                if (quantity >= slimit && quantity <= elimit) {

                    if (rentType.equalsIgnoreCase("1")) {
                        netTotal = total + charges;
                        double DistanceRoundOff = (double) Math.round(netTotal * 100) / 100;
                        decimalFormat = new DecimalFormat("##.##").format(DistanceRoundOff);
                        totalPriceOderDetail.setText("Rs. " + decimalFormat);
                        App.getSinltonPojo().setProductTotalPrice(String.valueOf(decimalFormat));
                    } else if (rentType.equalsIgnoreCase("2")) {
                        tempTotal = quantity * charges;
                        netTotal = total + tempTotal;
                        double DistanceRoundOff = (double) Math.round(netTotal * 100) / 100;
                        decimalFormat = new DecimalFormat("##.##").format(DistanceRoundOff);
                        totalPriceOderDetail.setText("Rs. " + decimalFormat);
                        App.getSinltonPojo().setProductTotalPrice(String.valueOf(decimalFormat));
                    }
                }
            } else if (App.getSinltonPojo().getProductStatus().equalsIgnoreCase("2")) {
                productNameOrderDetail.setText(App.getSinltonPojo().getProductName());
                locationOrderDetail.setText(App.getSinltonPojo().getProductLocation());
                perLitrePriceOrderDetail.setText(App.getSinltonPojo().getProductPrice());
                DropOrderDateOrderDetail.setText(App.getSinltonPojo().getProductDate());
                totalPriceOderDetail.setText(App.getSinltonPojo().getProductTotalPrice());

                double totalPrice = Double.parseDouble(App.getSinltonPojo().getProductTotalPrice());
                double perLitrePrice = Double.parseDouble(App.getSinltonPojo().getProductPrice());
                quan = totalPrice / perLitrePrice;

                if (quan >= slimit && quan < elimit) {

                    if (rentType.equalsIgnoreCase("1")) {
                        tempPrice = totalPrice - charges;
                        quan1 = tempPrice / perLitrePrice;

                        double DistanceRoundOff = (double) Math.round(quan1 * 100) / 100;
                        decimalFormat = new DecimalFormat("##.##").format(DistanceRoundOff);
                        quantityOrderDetail.setText(""+decimalFormat);
                        App.getSinltonPojo().setProductQuantity(String.valueOf(decimalFormat));

                    } else if (rentType.equalsIgnoreCase("2")) {

                        tempPrice = totalPrice - (quan * charges);
                        quan1 = tempPrice / perLitrePrice;
                        double DistanceRoundOff = (double) Math.round(quan1 * 100) / 100;
                        decimalFormat = new DecimalFormat("##.##").format(DistanceRoundOff);
                        quantityOrderDetail.setText(""+decimalFormat);
                        App.getSinltonPojo().setProductQuantity(String.valueOf(decimalFormat));

                    }

                }
            }
        } else {
            productNameOrderDetail.setText(App.getSinltonPojo().getProductName());
            quantityOrderDetail.setText(App.getSinltonPojo().getProductQuantity());
            locationOrderDetail.setText(App.getSinltonPojo().getProductLocation());
            perLitrePriceOrderDetail.setText(App.getSinltonPojo().getProductPrice());
            DropOrderDateOrderDetail.setText(App.getSinltonPojo().getProductDate());
            totalPriceOderDetail.setText(App.getSinltonPojo().getProductTotalPrice());

        }
    }

    private void initIds() {
        activity = OrderDetailsActivity.this;
        btn_proceed = findViewById(R.id.btn_proceed_to_payment);
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        btn_proceed.setOnClickListener(this);
        perLitrePriceOrderDetail = findViewById(R.id.perLitrePriceOrderDetail);
        DropOrderDateOrderDetail = findViewById(R.id.DropOrderDateOrderDetail);
        quantityOrderDetail = findViewById(R.id.quantityOrderDetail);
        totalPriceOderDetail = findViewById(R.id.totalPriceOderDetail);
        locationOrderDetail = findViewById(R.id.locationOrderDetail);
        productNameOrderDetail = findViewById(R.id.productNameOrderDetail);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed_to_payment:
                changeButtonBackground(btn_proceed, null);
                if (type.equalsIgnoreCase("payment")) {
                    App.getSinltonPojo().setTrackOrderStatus("2");
                    Intent intent = new Intent(OrderDetailsActivity.this, TrackOrderActivity.class);
                    startActivity(intent);
                } else if (type.equalsIgnoreCase("place_order")) {
                    App.getSinltonPojo().setReorderStatus("0");
                    Intent intent = new Intent(OrderDetailsActivity.this, PaymentMethodActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrderDetailsActivity.this, HomePageActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_proceed};
        unChangeButtonBackground(unChange);

    }

    private void changeButtonBackground(Button toChange, Button unChange[]) {

        toChange.setBackgroundResource(R.drawable.black_bg);
        toChange.setTextColor(getResources().getColor(R.color.white));
        if (unChange != null) {
            for (int i = 0; i < unChange.length; i++) {
                unChange[i].setBackgroundResource(R.drawable.grey_corners);
                unChange[i].setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void unChangeButtonBackground(Button unChange[]) {

        for (int i = 0; i < unChange.length; i++) {
            unChange[i].setBackgroundResource(R.drawable.grey_corners);
            unChange[i].setTextColor(getResources().getColor(R.color.black));
        }

    }
}
