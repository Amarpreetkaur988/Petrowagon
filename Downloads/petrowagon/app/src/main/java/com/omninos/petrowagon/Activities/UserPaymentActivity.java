package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.petrowagon.R;
import com.omninos.petrowagon.ViewModel.OrderViewModel;

public class UserPaymentActivity extends AppCompatActivity  {

    private Button btn_next, btnYesSure, btnNoSure;
    private TextView tv_head;
    private ImageView img_back;
    private LayoutInflater layoutInflater;
    private Button unChange[];
    private Activity activity;
    private EditText cardNumberPayment, cardMonthPayment, cardYearPayment, cardCVVPayment;
    private String cardNumberPay, cardMonthPay, cardYearPay, cardCVVPay;
    private int currentMonth, currentYear, cardYear, cardMonth;
    private int count = 0;
    private OrderViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_user);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

    }


}
