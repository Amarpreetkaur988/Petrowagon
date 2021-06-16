package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.CancelOrderModel;
import com.omninos.petrowagon.ModelClass.TrackOrderModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

public class TrackOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView trackOrderText, tv_head, tv_pro_name_track, tv_locationTrack, tv_quantityTrack, tv_dateTrack, tv_pricePerLitreTrack, confirm_text, fueling_text, delivery_text, delivered_text;
    private ImageView img_back, view_fueling, view_delivery_progress, view_delivered, view_orderConfirm;
    private OrderViewModel viewModel;
    private Activity activity;
    private View v1, v2, v3;
    private LinearLayout TrackOrderLl;
    private Button unChange[];
    private Button BtnCAncelOrder, cancelOrderOk, btnYesSure, btnNoSure;
    ;
    private LayoutInflater layoutInflater;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        initIds();

        tv_head.setText("TRACK ORDER");
        if (App.getSinltonPojo().getNotificetionBookingId() != null) {
            orderId = App.getSinltonPojo().getNotificetionBookingId();
            trackOrderDetail();
            App.getSinltonPojo().setNotificetionBookingId(null);
        } else {
            orderId = App.getSinltonPojo().getOrderId();
        }
        if (App.getSinltonPojo().getTrackOrderStatus() != null) {

            if (App.getSinltonPojo().getTrackOrderStatus().equalsIgnoreCase("1")) {
                orderId = App.getSinltonPojo().getOrderId();
                trackLatestOrderDetail();
            }
            if (App.getSinltonPojo().getTrackOrderStatus().equalsIgnoreCase("0")) {
                orderId = App.getSinltonPojo().getOrderId();
                trackOrderDetail();
            }
            if (App.getSinltonPojo().getTrackOrderStatus().equalsIgnoreCase("2")) {
                orderId = App.getSinltonPojo().getOrderId();
                nowBookOrder();
            }

        } else {

        }

    }


    private void initIds() {
//        TrackOrderLl = findViewById(R.id.TrackOrderLl);
        trackOrderText = findViewById(R.id.trackOrderText);
        tv_pro_name_track = findViewById(R.id.tv_pro_name_track);
        activity = TrackOrderActivity.this;
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BtnCAncelOrder = findViewById(R.id.BtnCAncelOrder);
        BtnCAncelOrder.setOnClickListener(this);
        tv_locationTrack = findViewById(R.id.tv_locationTrack);
        tv_quantityTrack = findViewById(R.id.tv_quantityTrack);
        tv_dateTrack = findViewById(R.id.tv_dateTrack);
        tv_pricePerLitreTrack = findViewById(R.id.tv_pricePerLitreTrack);
        view_fueling = findViewById(R.id.view_fueling);
        view_delivery_progress = findViewById(R.id.view_delivery_progress);
        view_delivered = findViewById(R.id.view_delivered);
        view_orderConfirm = findViewById(R.id.view_orderConfirm);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        confirm_text = findViewById(R.id.confirm_text);
        fueling_text = findViewById(R.id.fueling_text);
        delivery_text = findViewById(R.id.delivery_text);
        delivered_text = findViewById(R.id.delivered_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
//                onBackPressed();


                if (App.getSinltonPojo().getTrackOrderStatus() != null) {

                    if (App.getSinltonPojo().getTrackOrderStatus().equalsIgnoreCase("1")) {
                        onBackPressed();
                    }
                    if (App.getSinltonPojo().getTrackOrderStatus().equalsIgnoreCase("0")) {
                        onBackPressed();
                    }
                    if (App.getSinltonPojo().getTrackOrderStatus().equalsIgnoreCase("2")) {
                        startActivity(new Intent(TrackOrderActivity.this, HomePageActivity.class));
                    }

                } else {

                    startActivity(new Intent(TrackOrderActivity.this, HomePageActivity.class));
                    finish();


                }
                break;

            case R.id.BtnCAncelOrder:
                changeButtonBackground(BtnCAncelOrder, null);
                SureDialog();
                break;
        }
    }


    private void trackOrderDetail() {

        viewModel.trackOrder(activity, orderId).observe(TrackOrderActivity.this, new Observer<TrackOrderModel>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(TrackOrderModel trackOrderModel) {
                if (trackOrderModel.getSuccess().equalsIgnoreCase("1")) {
                    trackOrderText.setVisibility(View.GONE);

                    tv_locationTrack.setText(trackOrderModel.getDetails().getLocation());
                    tv_quantityTrack.setText(trackOrderModel.getDetails().getQuantity());
                    tv_dateTrack.setText(trackOrderModel.getDetails().getDate());
                    tv_pricePerLitreTrack.setText(trackOrderModel.getDetails().getPricePerLitre());
                    tv_pro_name_track.setText(trackOrderModel.getDetails().getTitle());
                    App.getSinltonPojo().setOrderId(trackOrderModel.getDetails().getId());
                    String status = trackOrderModel.getDetails().getOrderStatus();
                    if (status.equalsIgnoreCase("1")) {
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        view_delivered.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        confirm_text.setTextColor(getResources().getColor(R.color.black));
                        BtnCAncelOrder.setVisibility(View.VISIBLE);
                    }
                    if (status.equalsIgnoreCase("3")) {
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setImageResource(R.drawable.ic_check);
                        view_fueling.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        view_delivered.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        v1.setBackgroundColor(getResources().getColor(R.color.black));
                        BtnCAncelOrder.setVisibility(View.VISIBLE);
                    }
                    if (status.equalsIgnoreCase("4")) {
                        BtnCAncelOrder.setVisibility(View.GONE);
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setImageResource(R.drawable.ic_check);
                        view_fueling.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setImageResource(R.drawable.ic_check);
                        view_delivered.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        v1.setBackgroundColor(getResources().getColor(R.color.black));
                        v2.setBackgroundColor(getResources().getColor(R.color.black));

                    }
                    if (status.equalsIgnoreCase("5")) {
                        BtnCAncelOrder.setVisibility(View.GONE);
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setImageResource(R.drawable.ic_check);
                        view_fueling.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setImageResource(R.drawable.ic_check);
                        view_delivered.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivered.setImageResource(R.drawable.ic_check);
                        v1.setBackgroundColor(getResources().getColor(R.color.black));
                        v2.setBackgroundColor(getResources().getColor(R.color.black));
                        v3.setBackgroundColor(getResources().getColor(R.color.black));

                    }
                } else {
                    trackOrderText.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void trackLatestOrderDetail() {
        String userId = App.getAppPreference().getLoginDetail().getDetails().getId();


        viewModel.trackLatestOrder(activity, userId).observe(TrackOrderActivity.this, new Observer<TrackOrderModel>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(TrackOrderModel trackOrderModel) {
                if (trackOrderModel.getSuccess().equalsIgnoreCase("1")) {
                    trackOrderText.setVisibility(View.GONE);

                    tv_locationTrack.setText(trackOrderModel.getDetails().getLocation());
                    tv_quantityTrack.setText(trackOrderModel.getDetails().getQuantity());
                    tv_dateTrack.setText(trackOrderModel.getDetails().getDate());
                    tv_pro_name_track.setText(trackOrderModel.getDetails().getTitle());
                    tv_pricePerLitreTrack.setText(trackOrderModel.getDetails().getPricePerLitre());
                    String status = trackOrderModel.getDetails().getOrderStatus();
                    App.getSinltonPojo().setOrderId(trackOrderModel.getDetails().getId());
                    if (status.equalsIgnoreCase("1")) {
                        BtnCAncelOrder.setVisibility(View.VISIBLE);
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        view_delivered.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        confirm_text.setTextColor(getResources().getColor(R.color.black));
                    }
                    if (status.equalsIgnoreCase("3")) {
                        BtnCAncelOrder.setVisibility(View.VISIBLE);
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setImageResource(R.drawable.ic_check);
                        view_fueling.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        view_delivered.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        v1.setBackgroundColor(getResources().getColor(R.color.black));
                    }
                    if (status.equalsIgnoreCase("4")) {
                        BtnCAncelOrder.setVisibility(View.GONE);
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setImageResource(R.drawable.ic_check);
                        view_fueling.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setImageResource(R.drawable.ic_check);
                        view_delivered.setBackgroundResource(R.drawable.ic_circular_shape_silhouette);
                        v1.setBackgroundColor(getResources().getColor(R.color.black));
                        v2.setBackgroundColor(getResources().getColor(R.color.black));
                    }
                    if (status.equalsIgnoreCase("5")) {
                        BtnCAncelOrder.setVisibility(View.GONE);
                        view_orderConfirm.setImageResource(R.drawable.ic_check);
                        view_orderConfirm.setBackgroundResource(R.drawable.ic_black_circle);
                        view_fueling.setImageResource(R.drawable.ic_check);
                        view_fueling.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivery_progress.setImageResource(R.drawable.ic_check);
                        view_delivered.setBackgroundResource(R.drawable.ic_black_circle);
                        view_delivered.setImageResource(R.drawable.ic_check);
                        v1.setBackgroundColor(getResources().getColor(R.color.black));
                        v2.setBackgroundColor(getResources().getColor(R.color.black));
                        v3.setBackgroundColor(getResources().getColor(R.color.black));
                    }
                } else {
                    trackOrderText.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void cancelOrder() {
        String orderId = App.getSinltonPojo().getOrderId();
        viewModel.cancelOrder(activity, orderId).observe(TrackOrderActivity.this, new Observer<CancelOrderModel>() {
            @Override
            public void onChanged(CancelOrderModel cancelOrderModel) {
                if (cancelOrderModel.getSuccess().equalsIgnoreCase("1")) {
//                    startActivity(new Intent(TrackOrderActivity.this, HomePageActivity.class));
                    cancelDialog();

                } else {
                    Toast.makeText(activity, cancelOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void SureDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.are_you_sure_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(TrackOrderActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(true);
        dailogbox.setView(confirmdailog);
        btnYesSure = confirmdailog.findViewById(R.id.btnYesSure);
        btnYesSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnYesSure.setBackgroundResource(R.drawable.black_bg);
                btnYesSure.setTextColor(getResources().getColor(R.color.white));
                cancelOrder();
            }
        });
        btnNoSure = confirmdailog.findViewById(R.id.btnNoSure);
        btnNoSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNoSure.setBackgroundResource(R.drawable.black_bg);
                btnNoSure.setTextColor(getResources().getColor(R.color.white));
                dailogbox.dismiss();
            }
        });

        dailogbox.show();
    }

    private void cancelDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.cancel_order_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(TrackOrderActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setView(confirmdailog);

        cancelOrderOk = confirmdailog.findViewById(R.id.cancelOrderOk);
        cancelOrderOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrderOk.setBackgroundResource(R.drawable.black_bg);
                cancelOrderOk.setTextColor(getResources().getColor(R.color.white));
                startActivity(new Intent(TrackOrderActivity.this, HomePageActivity.class));
            }
        });
        dailogbox.show();

    }

    private void nowBookOrder() {
//        App.getSinltonPojo().setOrderId(trackOrderModel.getDetails().getId());
        tv_locationTrack.setText(App.getSinltonPojo().getProductLocation());
        tv_quantityTrack.setText(App.getSinltonPojo().getProductQuantity());
        tv_dateTrack.setText(App.getSinltonPojo().getProductDate());
        tv_pricePerLitreTrack.setText(App.getSinltonPojo().getProductPrice());
        tv_pro_name_track.setText(App.getSinltonPojo().getProductName());
        trackOrderText.setVisibility(View.GONE);
//        cancelOrderOk.setVisibility(View.GONE);

    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, HomePageActivity.class));
//    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{BtnCAncelOrder};
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
