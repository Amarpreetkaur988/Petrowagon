package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.ForgotPasswordModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.LoginRegisterViewModel;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_loginForgot;
    private String S_userEmail, S_userPassword;
    private EditText et_emailForgot;
    private Activity activity;
    private LoginRegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        findIds();
    }

    private void findIds() {
        ImageView back =  findViewById(R.id.img_back);
        back.setOnClickListener(this);
        activity = ForgotPasswordActivity.this;
        btn_loginForgot = findViewById(R.id.btn_loginForgot);
        btn_loginForgot.setOnClickListener(this);
        et_emailForgot = findViewById(R.id.et_emailForgot);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.btn_loginForgot:
                App.getSinltonPojo().setForgotPasswordStatus("1");
                validate();
                break;
        }
    }

    private void validate() {
        S_userEmail = et_emailForgot.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}";
        if (S_userEmail.isEmpty()) {
            et_emailForgot.setError("Enter your phone number");
        } else if (S_userEmail.length()<10) {
            et_emailForgot.setError("Enter valid phone number");
        } else {
            sendOTP();
        }
    }

    private void sendOTP() {
        viewModel.forgotPassword(activity, S_userEmail).observe(ForgotPasswordActivity.this, new Observer<ForgotPasswordModel>() {
            @Override
            public void onChanged(ForgotPasswordModel forgotPasswordModel) {
                if (forgotPasswordModel.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(activity, forgotPasswordModel.getMessage(), Toast.LENGTH_SHORT).show();
                    App.getAppPreference().clearAppPreferences();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(activity, forgotPasswordModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
