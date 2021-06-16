package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.ViewModel.DescriptionViewModel;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_head;
    private WebView aboutUsText;
    private ImageView img_back;
    private DescriptionViewModel viewModel;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        viewModel = ViewModelProviders.of(this).get(DescriptionViewModel.class);

        initIds();

        if (App.getSinltonPojo().getWebViewStatus()!=null) {

            if (App.getSinltonPojo().getWebViewStatus().equalsIgnoreCase("1")){
                aboutUsText.loadUrl("http://petrowagon.com/petrowagonApplication/index.php/api/user/terms");
                tv_head.setText("Terms & Conditions");
            }
            if (App.getSinltonPojo().getWebViewStatus().equalsIgnoreCase("2")){
                aboutUsText.loadUrl("http://petrowagon.com/petrowagonApplication/index.php/api/user/privacyAndPolicy");
                tv_head.setText("Privacy Policy");
            }
            if (App.getSinltonPojo().getWebViewStatus().equalsIgnoreCase("3")){
                aboutUsText.loadUrl("http://petrowagon.com/petrowagonApplication/index.php/api/user/aboutUs");
                tv_head.setText("About Us");
            }
        }
        else {
            tv_head.setText("");
        }
    }

    private void initIds() {
        activity = AboutUsActivity.this;
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        aboutUsText = findViewById(R.id.aboutUsText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }


}
