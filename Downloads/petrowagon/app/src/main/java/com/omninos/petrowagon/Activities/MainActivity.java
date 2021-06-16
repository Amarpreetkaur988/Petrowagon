package com.omninos.petrowagon.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.omninos.petrowagon.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_login_main,btn_reg_main;
    private Button unChange[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initIds();

    }

    private void initIds() {
        btn_login_main=findViewById(R.id.btn_login_main);
        btn_reg_main=findViewById(R.id.btn_reg_main);

        btn_login_main.setOnClickListener(this);
        btn_reg_main.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_main:
                unChange = new Button[]{btn_reg_main};
                changeButtonBackground(btn_login_main, unChange);
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;

            case R.id.btn_reg_main:
                unChange = new Button[]{btn_login_main};
                changeButtonBackground(btn_reg_main, unChange);
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_login_main,btn_reg_main};
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
