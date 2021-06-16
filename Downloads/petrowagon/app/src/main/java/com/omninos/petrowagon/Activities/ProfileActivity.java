package com.omninos.petrowagon.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.omninos.petrowagon.ModelClass.LoginRegisterModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.AppConstants;
import com.omninos.petrowagon.ViewModel.LoginRegisterViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECT_IMAGE = 1;
    private TextView tv_head;
    private Button btn_save;
    private ImageView img_back, img_edit, img_add,img_profile;
    private EditText et_name,et_address,et_email,et_phone;
    private Button unChange[];
    private LoginRegisterViewModel viewModel;
    private String imagepath="", S_name, S_phone, S_address,S_email;
    private Activity activity;
    private ProgressBar profileProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);

        initIds();
        img_edit.setVisibility(View.VISIBLE);
        tv_head.setText("PROFILE");
    }



    private void permissions() {
        if (ActivityCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.CAMERA + Manifest.permission.WRITE_EXTERNAL_STORAGE +
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
//            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean write = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (grantResults.length > 0 && camera && write && read) {
//                    init();
                    OpenGallery();
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    OpenSetting();

                } else {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
        }

    }

    private void OpenSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission");
        builder.setMessage("Permissions are required");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(ProfileActivity.this, "Go to Settings to Grant the Storage Permissions and restart application", Toast.LENGTH_LONG).show();
//                sentToSettings = true;
                //move to setting for permission
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", ProfileActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        })
                .create()
                .show();
    }






    private void initIds() {
        activity = ProfileActivity.this;
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);
        img_edit = findViewById(R.id.img_edit);
        img_add = findViewById(R.id.img_add_profile);
        btn_save = findViewById(R.id.btn_save_profile);

        img_profile=findViewById(R.id.img_profile);
        et_name=findViewById(R.id.et_name_profile);
        et_address=findViewById(R.id.et_address_profile);
        et_email=findViewById(R.id.et_email_profile);
        et_phone=findViewById(R.id.et_phone_profile);
        profileProgress = findViewById(R.id.profileProgress);

        et_name.setText(App.getAppPreference().getLoginDetail().getDetails().getName());
        et_email.setText(App.getAppPreference().getLoginDetail().getDetails().getEmail());
        et_phone.setText(App.getAppPreference().getLoginDetail().getDetails().getPhone());
        et_address.setText(App.getAppPreference().getLoginDetail().getDetails().getAddress());

        if (App.getAppPreference().getLoginDetail().getDetails().getImage().contains("https:")){
            profileProgress.setVisibility(View.GONE);
            Glide.with(this).load(App.getAppPreference().getLoginDetail().getDetails().getImage());
        }else {
            if (App.getAppPreference().getLoginDetail().getDetails().getImage().equals("")) {
                profileProgress.setVisibility(View.GONE);
                img_profile.setImageResource(R.drawable.dummy_img);
            } else {
//            Picasso.get().load("http://petrowagon.com/petrowagonApplication/"+App.getAppPreference().getLoginDetail().getDetails().getImage()).into(img_profile);
                Glide.with(this).load("http://petrowagon.com/petrowagonApplication/" + App.getAppPreference().getLoginDetail().getDetails().getImage()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        profileProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        profileProgress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(img_profile);
            }
        }

        img_add.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.img_edit:
                tv_head.setText("EDIT PROFILE");
                img_edit.setVisibility(View.GONE);
                img_add.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                et_name.setEnabled(true);
                et_phone.setEnabled(true);
                et_email.setEnabled(true);
                et_address.setEnabled(true);
                et_name.requestFocus();
                break;

            case R.id.btn_save_profile:
                changeButtonBackground(btn_save, null);
                tv_head.setText("PROFILE");
                et_name.setEnabled(false);
                et_phone.setEnabled(false);
                et_email.setEnabled(false);
                et_address.setEnabled(false);
                img_edit.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                validate();
                break;

            case R.id.img_add_profile:
                permissions();
                break;
        }
    }

    private void validate() {
        S_name = et_name.getText().toString();
        S_email = et_email.getText().toString();
        S_phone = et_phone.getText().toString();
        S_address = et_address.getText().toString();
        if (S_phone.length()<10){
            et_phone.setError("Please enter valid phone number");
        }
        else {
            updateProfile();
        }
    }

    private void updateProfile() {
        MultipartBody.Part body;
        File file = new File(imagepath);
        if (!imagepath.equalsIgnoreCase("")) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        RequestBody name = RequestBody.create(MediaType.get("text/plain"), S_name);
        RequestBody phone = RequestBody.create(MediaType.get("text/plain"), S_phone);
        RequestBody address = RequestBody.create(MediaType.get("text/plain"), S_address);
        RequestBody email = RequestBody.create(MediaType.get("text/plain"), S_email);
        RequestBody userId = RequestBody.create(MediaType.get("text/plain"), App.getAppPreference().getLoginDetail().getDetails().getId());

        viewModel.editProfile(activity, userId, name, phone, email, address, body).observe(ProfileActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")){
                    App.getAppPreference().saveLoginDetail(loginRegisterModel);
                    App.getAppPreference().saveString(AppConstants.TOKEN,"1");
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenGallery(){

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                String resultUri = result.getUri().getPath();
                Glide.with(this).load(resultUri).into(img_profile);
                imagepath = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_save};
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