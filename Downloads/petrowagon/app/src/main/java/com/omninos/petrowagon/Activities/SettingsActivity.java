package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.NotificationModel;
import com.omninos.petrowagon.ModelClass.UserSettingsModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.AppConstants;
import com.omninos.petrowagon.SharePreference.CommonUtils;
import com.omninos.petrowagon.ViewModel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView tv_head;
    private CardView ChangePassword_Cv;
    private ImageView img_back;
    private Switch pushNotification, smsMessage, productEmail, marketingEmail;
    private SettingsViewModel viewModel;
    private Activity activity;
    private String strSms, strProductEmail, strMarketingEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        initIds();
        tv_head.setText("SETTINGS");
        setData();
    }

    private void initIds() {
        activity = SettingsActivity.this;
        tv_head=findViewById(R.id.tv_head_topbar);
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
//        logOut_Tv = findViewById(R.id.logOut_Tv);
//        logOut_Tv.setOnClickListener(this);
        pushNotification = findViewById(R.id.pushNotification);
        pushNotification.setOnClickListener(this);
        productEmail = findViewById(R.id.productEmail);
        productEmail.setOnClickListener(this);
        marketingEmail = findViewById(R.id.marketingEmail);
        marketingEmail.setOnClickListener(this);
        ChangePassword_Cv = findViewById(R.id.ChangePassword_Cv);
        ChangePassword_Cv.setOnClickListener(this);
    }

    private void setData() {
        if (App.getAppPreference().isUserFirstTime()) {
//            CommonUtils.showProgress(activity, "Please wait...");
            viewModel.userSetting(activity, App.getAppPreference().getLoginDetail().getDetails().getId()).observe(this, new Observer<UserSettingsModel>() {
                @Override
                public void onChanged(UserSettingsModel userSettingsModel) {
//                    CommonUtils.dismissProgress();
                    if (userSettingsModel != null) {

                        if (userSettingsModel.getSuccess().equalsIgnoreCase("1")) {

                            App.getAppPreference().setUserLoginTime();
                            if (userSettingsModel.getDetails().getNstatus().equalsIgnoreCase("1")) {
                                pushNotification.setChecked(true);
                                App.getAppPreference().setSettings(AppConstants.PUSH_NOTIFICATION, true);
                            } else {
                                pushNotification.setChecked(false);
                                App.getAppPreference().setSettings(AppConstants.PUSH_NOTIFICATION, false);
                            }

                            if (userSettingsModel.getDetails().getProductEmails().equalsIgnoreCase("1")) {
                                productEmail.setChecked(true);
                                App.getAppPreference().setSettings(AppConstants.PRODUCT_EMAIL, true);
                            } else {
                                productEmail.setChecked(false);
                                App.getAppPreference().setSettings(AppConstants.PRODUCT_EMAIL, false);
                            }

                            if (userSettingsModel.getDetails().getMarketingEmails().equalsIgnoreCase("1")) {
                                marketingEmail.setChecked(true);
                                App.getAppPreference().setSettings(AppConstants.MARKETING_EMAIL, true);
                            } else {
                                marketingEmail.setChecked(false);
                                App.getAppPreference().setSettings(AppConstants.MARKETING_EMAIL, false);
                            }
                        }
                    } else {
                        CommonUtils.showAlert(activity, "Something went wrong");
                    }

                }
            });
        } else {


            pushNotification.setChecked(App.getAppPreference().getSettings(AppConstants.PUSH_NOTIFICATION));
            productEmail.setChecked(App.getAppPreference().getSettings(AppConstants.PRODUCT_EMAIL));
            marketingEmail.setChecked(App.getAppPreference().getSettings(AppConstants.MARKETING_EMAIL));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.pushNotification:
                App.getAppPreference().setSettings(AppConstants.PUSH_NOTIFICATION, pushNotification.isChecked());
                setPushNotification();
                break;

            case R.id.productEmail:
                if (isInternet()) {
                    App.getAppPreference().setSettings(AppConstants.PRODUCT_EMAIL, productEmail.isChecked());
                    prepairData();
                    changeSettings();
                } else {
                    productEmail.setChecked(!productEmail.isChecked());
                }

                break;

            case R.id.marketingEmail:
                if (isInternet()) {
                    App.getAppPreference().setSettings(AppConstants.MARKETING_EMAIL, marketingEmail.isChecked());
                    prepairData();
                    changeSettings();
                } else {
                    marketingEmail.setChecked(!marketingEmail.isChecked());
                }
                break;

            case R.id.ChangePassword_Cv:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
        }
    }

    private void setPushNotification() {
        String status = "";
        if (pushNotification.isChecked()) {
            status = "1";
        } else {
            status = "0";
        }
        if (isInternet()) {
            viewModel.pushnotification(activity, App.getAppPreference().getLoginDetail().getDetails().getId(), status).observe(this, new Observer<NotificationModel>() {
                @Override
                public void onChanged(NotificationModel notificationModel) {
                    if (notificationModel != null) {
                        Toast.makeText(activity, notificationModel.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void prepairData() {
        if (productEmail.isChecked()) strProductEmail = "1";
        else strProductEmail = "0";

        if (marketingEmail.isChecked()) strMarketingEmail = "1";
        else strMarketingEmail = "0";
    }

    private void changeSettings() {

        viewModel.changeSetting(activity,strProductEmail, strMarketingEmail,App.getAppPreference().getLoginDetail().getDetails().getId()).observe(this, map -> {
            if (map != null) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    Toast.makeText(activity, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.productEmail:
                if (!isInternet()) {
                    productEmail.setChecked(!isChecked);
                }

                break;

            case R.id.marketingEmail:
                if (!isInternet()) {
                    marketingEmail.setChecked(!isChecked);
                }
                break;
        }
    }

    private boolean isInternet() {
        if (CommonUtils.isNetworkConnected(activity)) {
            return true;

        } else {
            return false;
        }
    }
}
