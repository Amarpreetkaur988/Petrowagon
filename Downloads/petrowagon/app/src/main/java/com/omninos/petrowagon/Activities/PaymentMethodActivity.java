package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.PlaceOrderModel;
import com.omninos.petrowagon.ModelClass.ReorderProductModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.AppConstants;
import com.omninos.petrowagon.ViewModel.OrderViewModel;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class PaymentMethodActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_next_payment_method, btnYesSure, btnNoSure;
    private TextView tv_head;
    private ImageView img_back;
    private Button unChange[];
    private RadioButton cashOnDelivery, debitCreditCard, paytm;
    private LayoutInflater layoutInflater;
    private OrderViewModel viewModel;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        initIds();
        tv_head.setText("PAYMENT");
    }

    private void initIds() {
        activity = PaymentMethodActivity.this;
        btn_next_payment_method = findViewById(R.id.btn_next_payment_method);
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        btn_next_payment_method.setOnClickListener(this);
        img_back.setOnClickListener(this);
        cashOnDelivery = findViewById(R.id.cashOnDelivery);
        debitCreditCard = findViewById(R.id.debitCreditCard);
//        paytm = findViewById(R.id.paytm);

        cashOnDelivery.setChecked(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_payment_method:
                changeButtonBackground(btn_next_payment_method, null);
//
                if (cashOnDelivery.isChecked()) {
                    App.getSinltonPojo().setPaymentMethod("1");
//                    Toast.makeText(this, "Cash on Delivery", Toast.LENGTH_SHORT).show();
                    SureDialog();
                }
                if (debitCreditCard.isChecked()) {
                    App.getSinltonPojo().setPaymentMethod("2");
                    SureDialog();
//                    onlinePayment();

                }
//                if (paytm.isChecked()) {
//                    App.getSinltonPojo().setPaymentMethod("3");
//                    Toast.makeText(this, "Paytm", Toast.LENGTH_SHORT).show();
//                    SureDialog();
//                }
                break;

            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private void SureDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.are_you_sure_popup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(PaymentMethodActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(true);
        dailogbox.setView(confirmdailog);
        btnYesSure = confirmdailog.findViewById(R.id.btnYesSure);
        btnYesSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnYesSure.setBackgroundResource(R.drawable.black_bg);
                btnYesSure.setTextColor(getResources().getColor(R.color.white));
                if (App.getSinltonPojo().getPaymentMethod().equalsIgnoreCase("1")) {
                    cashPayment();
                }
                if (App.getSinltonPojo().getPaymentMethod().equalsIgnoreCase("2")) {
                    onlinePayment();
                }
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

    private void cashPayment() {

        if (App.getSinltonPojo().getReorderStatus().equalsIgnoreCase("0")) {

            String userId, productId, quantity, location, date, pricePerLitre, totalPrice, paymentMethod, status, time, token, lat, lng;
            userId = App.getAppPreference().getLoginDetail().getDetails().getId();
            productId = App.getSinltonPojo().getProductId();
            quantity = App.getSinltonPojo().getProductQuantity();
            status = App.getSinltonPojo().getProductStatus();
            location = App.getSinltonPojo().getProductLocation();
            date = App.getSinltonPojo().getProductDate();
            time = App.getSinltonPojo().getProductTime();
            pricePerLitre = App.getSinltonPojo().getProductPrice();
            totalPrice = App.getSinltonPojo().getProductTotalPrice();
            paymentMethod = App.getSinltonPojo().getPaymentMethod();
            token = "";
            lat = App.getSinltonPojo().getCurrentLat();
            lng = App.getSinltonPojo().getCurrentLong();
            String transactionId = "";

            viewModel.placeOrder(activity, userId, productId, quantity, status, location, date, time, pricePerLitre, totalPrice, paymentMethod, token, lat, lng, transactionId).observe(PaymentMethodActivity.this, new Observer<PlaceOrderModel>() {
                @Override
                public void onChanged(PlaceOrderModel placeOrderModel) {
                    if (placeOrderModel.getSuccess().equalsIgnoreCase("1")) {
//                    Toast.makeText(activity, placeOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                        ShowDialog();
                    } else {
                        Toast.makeText(activity, placeOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        if (App.getSinltonPojo().getReorderStatus().equalsIgnoreCase("1")) {

            String userId, date, paymentMethod, time, orderId, location, lat, lng;
            userId = App.getAppPreference().getLoginDetail().getDetails().getId();
            orderId = App.getSinltonPojo().getOrderId();
            date = App.getSinltonPojo().getProductDate();
            time = App.getSinltonPojo().getProductTime();
            location = App.getSinltonPojo().getProductLocation();
            paymentMethod = App.getSinltonPojo().getPaymentMethod();
            lat = App.getSinltonPojo().getCurrentLat();
            lng = App.getSinltonPojo().getCurrentLong();
            String transactionId = "";

            viewModel.reorderProduct(activity, userId, orderId, date, time, location, paymentMethod, lat, lng, transactionId).observe(PaymentMethodActivity.this, new Observer<ReorderProductModel>() {
                @Override
                public void onChanged(ReorderProductModel reorderProductModel) {
                    if (reorderProductModel.getSuccess().equalsIgnoreCase("1")) {
//                    Toast.makeText(activity, reorderProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                        ShowDialog();
                    } else {
                        Toast.makeText(activity, reorderProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void ShowDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_payment_successfull, null);
        final Dialog dailogbox = new Dialog(PaymentMethodActivity.this,R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setContentView(confirmdailog);

        confirmdailog.findViewById(R.id.btn_track_your_order_dialog_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentMethodActivity.this, OrderDetailsActivity.class);
                intent.putExtra("type", "payment");
                startActivity(intent);
            }
        });
        dailogbox.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_next_payment_method};
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


    private void onlinePayment() {

        final int random = new Random().nextInt(99999) + 69;
        String orderId = "00" + random;
        initilizePayment(orderId, App.getSinltonPojo().getProductTotalPrice());
    }

    private void initilizePayment(String orderId, String totalAmount) {
        if (App.getAppPreference().getLoginDetail().getDetails().getEmail().isEmpty()) {
            Toast.makeText(activity, "Please Update your Email first", Toast.LENGTH_SHORT).show();
        }
        if (App.getAppPreference().getLoginDetail().getDetails().getPhone().isEmpty()) {
            Toast.makeText(activity, "Please Update your Phone first", Toast.LENGTH_SHORT).show();
        } else {
            PaymentParams pgPaymentParams = new PaymentParams();
            pgPaymentParams.setAPiKey(AppConstants.PG_API_KEY);
            pgPaymentParams.setAmount(totalAmount);
            pgPaymentParams.setEmail(App.getAppPreference().getLoginDetail().getDetails().getEmail());
            pgPaymentParams.setName(App.getAppPreference().getLoginDetail().getDetails().getName());
            pgPaymentParams.setPhone(App.getAppPreference().getLoginDetail().getDetails().getPhone());
            pgPaymentParams.setOrderId(orderId);
            pgPaymentParams.setCurrency(AppConstants.PG_CURRENCY);
            pgPaymentParams.setDescription("Payment");
            pgPaymentParams.setCity(App.getSinltonPojo().getmCity());
            pgPaymentParams.setState(App.getSinltonPojo().getmState());
//        pgPaymentParams.setAddressLine1(App.getAppPreference().getLoginDetail().getDetails().getAddress());
//        pgPaymentParams.setAddressLine2("City Bussiness Centre");
            pgPaymentParams.setZipCode(App.getSinltonPojo().getmZipCode());
            pgPaymentParams.setCountry(App.getSinltonPojo().getmCountry());
            pgPaymentParams.setReturnUrl("http://futurewingsvisa.in/petroWagon/index.php/api/user/hashCreate");
            pgPaymentParams.setMode("LIVE");
//        pgPaymentParams.setUdf1(SampleAppConstants.PG_UDF1);
//        pgPaymentParams.setUdf2(SampleAppConstants.PG_UDF2);
//        pgPaymentParams.setUdf3(SampleAppConstants.PG_UDF3);
//        pgPaymentParams.setUdf4(SampleAppConstants.PG_UDF4);
//        pgPaymentParams.setUdf5(SampleAppConstants.PG_UDF5);

            PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams, PaymentMethodActivity.this);
            pgPaymentInitialzer.initiatePaymentProcess();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String paymentResponse = data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: " + paymentResponse);
                    if (paymentResponse.equals("null")) {
                        System.out.println("Transaction Error!");
                    } else {
                        JSONObject response = new JSONObject(paymentResponse);
                        String hash = response.getString("hash");
                        String transectionId = response.getString("transaction_id");
                        String transectionStatus = response.getString("response_message");
                        if (transectionStatus.equalsIgnoreCase("Transaction successful")) {
                            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();


                            if (App.getSinltonPojo().getReorderStatus().equalsIgnoreCase("1")) {

                                String userId, date, paymentMethod, time, orderId, location, lat, lng;
                                userId = App.getAppPreference().getLoginDetail().getDetails().getId();
                                orderId = App.getSinltonPojo().getOrderId();
                                date = App.getSinltonPojo().getProductDate();
                                time = App.getSinltonPojo().getProductTime();
                                location = App.getSinltonPojo().getProductLocation();
                                paymentMethod = App.getSinltonPojo().getPaymentMethod();
                                lat = App.getSinltonPojo().getCurrentLat();
                                lng = App.getSinltonPojo().getCurrentLong();

                                viewModel.reorderProduct(activity, userId, orderId, date, time, location, paymentMethod, lat, lng, transectionId).observe(PaymentMethodActivity.this, new Observer<ReorderProductModel>() {
                                    @Override
                                    public void onChanged(ReorderProductModel reorderProductModel) {
                                        if (reorderProductModel.getSuccess().equalsIgnoreCase("1")) {
//                    Toast.makeText(activity, reorderProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                                            ShowDialog();
                                        } else {
                                            Toast.makeText(activity, reorderProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                            if (App.getSinltonPojo().getReorderStatus().equalsIgnoreCase("0")) {

                                String userId, productId, quantity, location, date, pricePerLitre, totalPrice, paymentMethod, status, time, token, lat, lng;
                                userId = App.getAppPreference().getLoginDetail().getDetails().getId();
                                productId = App.getSinltonPojo().getProductId();
                                quantity = App.getSinltonPojo().getProductQuantity();
                                status = App.getSinltonPojo().getProductStatus();
                                location = App.getSinltonPojo().getProductLocation();
                                date = App.getSinltonPojo().getProductDate();
                                time = App.getSinltonPojo().getProductTime();
                                pricePerLitre = App.getSinltonPojo().getProductPrice();
                                totalPrice = App.getSinltonPojo().getProductTotalPrice();
                                paymentMethod = App.getSinltonPojo().getPaymentMethod();
                                token = "";
                                lat = App.getSinltonPojo().getCurrentLat();
                                lng = App.getSinltonPojo().getCurrentLong();

                                viewModel.placeOrder(activity, userId, productId, quantity, status, location, date, time, pricePerLitre, totalPrice, paymentMethod, token, lat, lng, transectionId).observe(PaymentMethodActivity.this, new Observer<PlaceOrderModel>() {
                                    @Override
                                    public void onChanged(PlaceOrderModel placeOrderModel) {
                                        if (placeOrderModel.getSuccess().equalsIgnoreCase("1")) {
//                    Toast.makeText(activity, placeOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                                            ShowDialog();
                                        } else {
                                            Toast.makeText(activity, placeOrderModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }

        }
    }
}
