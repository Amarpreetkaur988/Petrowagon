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
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.LoginRegisterModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.LoginRegisterViewModel;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_reg_reg;
    private TextView tv_head, terms, privcyPolicy;
    private ImageView img_back;
    private Button unChange[];
    private EditText userName, lastname, userPhone, userEmail, userPassword, userConfirmPassword ;
    private String S_userName,S_lastname, S_userPhone, S_userEmail, S_userPassword, S_userConfirmPassword;
    private LoginRegisterViewModel viewModel;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);

        initIds();
        tv_head.setText("REGISTER");
    }

    private void initIds() {
        activity = RegisterActivity.this;
        btn_reg_reg=findViewById(R.id.btn_reg_reg);
        tv_head=findViewById(R.id.tv_head_topbar);
        img_back=findViewById(R.id.img_back);

        img_back.setOnClickListener(this);
        btn_reg_reg.setOnClickListener(this);
        userName = findViewById(R.id.et_username_reg);
        userPhone = findViewById(R.id.et_phone_reg);
        userEmail = findViewById(R.id.et_email_reg);
        userPassword = findViewById(R.id.et_pass_reg);
        userConfirmPassword = findViewById(R.id.et_conf_pass_reg);
        terms = findViewById(R.id.terms);
        terms.setOnClickListener(this);
        privcyPolicy = findViewById(R.id.privcyPolicy);
        privcyPolicy.setOnClickListener(this);
        lastname = findViewById(R.id.et_lastname_reg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reg_reg:
                changeButtonBackground(btn_reg_reg, null);
//                Intent intent=new Intent(this,OTPVerificationActivity.class);
//                startActivity(intent);
                validate();
                break;

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.privcyPolicy:
                App.getSinltonPojo().setWebViewStatus("2");
                privacy();
                break;

            case R.id.terms:
                App.getSinltonPojo().setWebViewStatus("1");
                termsCondition();
                break;
        }
    }

    private void privacy() {
        startActivity(new Intent(RegisterActivity.this, AboutUsActivity.class));

    }

    private void termsCondition(){
        startActivity(new Intent(RegisterActivity.this, AboutUsActivity.class));
    }

    private void validate() {
        S_userName = userName.getText().toString().trim();
        S_lastname = lastname.getText().toString().trim();
        S_userPhone = userPhone.getText().toString().trim();
        S_userEmail = userEmail.getText().toString().trim();
        S_userPassword = userPassword.getText().toString().trim();
        S_userConfirmPassword = userConfirmPassword.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}";

        if (S_userName.isEmpty()) {
            userName.setError("Enter User Name");
        } else if (S_userPhone.isEmpty()){
            userPhone.setError("Please Enter your Phone Number");
        } else if (S_userEmail.isEmpty()) {
            userEmail.setError("Enter email");
        } else if (S_userEmail.isEmpty() || !S_userEmail.matches(emailPattern)) {
            userEmail.setError("Enter valid email");
        } else if (S_userPassword.isEmpty() || S_userPassword.length() < 7) {
            userPassword.setError("enter minimum 7 character password");
        } else if (S_userConfirmPassword.isEmpty()) {
            userConfirmPassword.setError("Confirm password");
        } else if (!S_userConfirmPassword.equals(S_userPassword)) {
            userConfirmPassword.setError("password mismatch");
        }
        else {
            userRegister();
        }
    }

    private void userRegister() {
        String regId = FirebaseInstanceId.getInstance().getToken();
        String fullName = S_userName+" "+S_lastname;

        viewModel.registerUser(activity,fullName, S_userPhone, S_userEmail, S_userPassword,regId, "Android","Normal").observe(RegisterActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")){

                    App.getSinltonPojo().setRegisterClass(loginRegisterModel);
                    App.getSinltonPojo().setRegisterStatus("register");
                    Intent intent = new Intent(activity, OTPVerificationActivity.class);
//                    intent.putExtra("phone",loginRegisterModel.getDetails().getPhone());
//                    intent.putExtra("otp",loginRegisterModel.getDetails().getOtp());
//                    Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finishAffinity();

                }else {
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_reg_reg};
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
