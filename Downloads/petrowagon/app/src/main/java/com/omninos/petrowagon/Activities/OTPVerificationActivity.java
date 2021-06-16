package com.omninos.petrowagon.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.VarificationTokenModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.AppConstants;
import com.omninos.petrowagon.ViewModel.LoginRegisterViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

public class  OTPVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_submit_otp;
    private LinearLayout lay_resend_code;
    private LayoutInflater layoutInflater;
    private TextView tv_head;
    private ImageView img_back;
    private Button unChange[];
    private EditText otp1, otp2, otp3, otp4;
    private LoginRegisterViewModel viewModel;
    private Activity activity;
    private RelativeLayout rl_otp_main;
    String not;
    String id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);

        initIds();



        if (App.getSinltonPojo().getRegisterStatus()!=null){
            if (App.getSinltonPojo().getRegisterStatus().equalsIgnoreCase("register")){
                id = App.getSinltonPojo().getRegisterClass().getDetails().getId();
            }
            else if (App.getSinltonPojo().getRegisterStatus().equalsIgnoreCase("login")){
                not = getIntent().getExtras().getString("notVerified");
                if (not.equalsIgnoreCase("1")){
                    id = getIntent().getExtras().getString("id");
                    resendOTP();
                }
                else {
                    id = App.getSinltonPojo().getRegisterClass().getDetails().getId();
                }
            }
        }

        OTPCode();
        tv_head.setText("VERIFICATION");

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initIds() {
        activity = OTPVerificationActivity.this;
        rl_otp_main = findViewById(R.id.rl_otp_main);
        btn_submit_otp = findViewById(R.id.btn_submit_otp);
        layoutInflater = LayoutInflater.from(OTPVerificationActivity.this);
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(this);
        btn_submit_otp.setOnClickListener(this);
        lay_resend_code = findViewById(R.id.lay_resend_code);
        lay_resend_code.setOnClickListener(this);

        otp1 = findViewById(R.id.otp1);
        otp1.requestFocus();
        otp1.setShowSoftInputOnFocus(true);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_otp:
//                changeButtonBackground(btn_submit_otp, null);
//                ShowDialog();
                VerifyOTP();
                break;

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.lay_resend_code:
                resendOTP();
                break;
        }
    }

    private void VerifyOTP() {

        String OTP1 = otp1.getText().toString();
        String OTP2 = otp2.getText().toString();
        String OTP3 = otp3.getText().toString();
        String OTP4 = otp4.getText().toString();
        String TOKEN = OTP1 + OTP2 + OTP3 + OTP4;

//        String id = App.getSinltonPojo().getRegisterClass().getDetails().getId();
        viewModel.token(activity, id, TOKEN).observe(OTPVerificationActivity.this, new Observer<VarificationTokenModel>() {
            @Override
            public void onChanged(VarificationTokenModel varificationTokenModel) {
                if (varificationTokenModel.getSuccess().equalsIgnoreCase("1")) {
                    App.getAppPreference().saveLoginDetail(App.getSinltonPojo().getRegisterClass());
                    App.getAppPreference().saveString(AppConstants.TOKEN, "1");
//                    Toast.makeText(activity, varificationTokenModel.getMessage(), Toast.LENGTH_SHORT).show();
                    ShowDialog();
                }
                else {
                    Snackbar snackbar = Snackbar.make(rl_otp_main, varificationTokenModel.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void resendOTP() {
        viewModel.resendOtp(activity, id).observe(OTPVerificationActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    App.getSinltonPojo().setResendOTP(map.get("otp").toString());
//                     Toast.makeText(activity, map.get("otp").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OTPCode() {

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    otp2.requestFocus();
                    otp1.setElevation(1);
                } else if (s.length() == 0) {

                }
            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp3.requestFocus();
                    otp2.setElevation(1);
                } else if (s.length() == 0) {

                    otp1.requestFocus();
                }
            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    otp4.requestFocus();
                    otp3.setElevation(1);
                } else if (s.length() == 0) {
                    otp2.requestFocus();
                }
            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    otp4.setElevation(1);
                } else if (s.length() == 0) {
                    otp3.requestFocus();
                }
            }
        });

    }

    private void ShowDialog() {

        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_verify, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(OTPVerificationActivity.this).create();
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setCancelable(false);
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.btn_done_dialog_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPVerificationActivity.this, HomePageActivity.class);
                startActivity(intent);
                finishAffinity();
                Toast.makeText(OTPVerificationActivity.this, "User Register Successfully", Toast.LENGTH_LONG).show();

            }
        });
        dailogbox.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_submit_otp};
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
