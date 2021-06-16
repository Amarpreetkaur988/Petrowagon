package com.omninos.petrowagon.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.petrowagon.ModelClass.LoginRegisterModel;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.SharePreference.App;
import com.omninos.petrowagon.SharePreference.AppConstants;
import com.omninos.petrowagon.SharePreference.CommonUtils;
import com.omninos.petrowagon.ViewModel.LoginRegisterViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login_login;
    private TextView tv_head, Tv_forgotPassword;
    private ImageView img_back;
    private Button unChange[];
    private EditText email_Login, password_login;
    private String S_userEmail, S_userPassword;
    private LoginRegisterViewModel viewModel;
    private ImageView img_fb_login, img_google_login;
    private Activity activity;
    private CallbackManager callbackManager;
    private String fbId = "", fbFirstName = "", fbLastName = "", fbEmail = "", fbSocialUserName = "", fbPhoneNumber = "", fbGender = "", fbDateOfBirth = "", fbCountry = "", fbProfilePicture = "";
    private static final int RC_SIGN_IN = 007;
    private String userName = "", userStringEmail = "", socialId = "", loginType, userImage = "", phoneNumber = "", gender = "", dateofbirth = "", country;
    private GoogleSignInClient mGoogleSignInClient;
    private URL fbProfilePicturenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        initIds();
        tv_head.setText("LOGIN");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FacebookSdk.sdkInitialize(getApplication());
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();
    }

    private void initIds() {
        activity = LoginActivity.this;
        btn_login_login = findViewById(R.id.btn_login_login);
        tv_head = findViewById(R.id.tv_head_topbar);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(this);
        btn_login_login.setOnClickListener(this);
        email_Login = findViewById(R.id.et_email_login);
        password_login = findViewById(R.id.et_pass_login);
        img_fb_login = findViewById(R.id.img_fb_login);
        img_fb_login.setOnClickListener(this);
        img_google_login = findViewById(R.id.img_google_login);
        img_google_login.setOnClickListener(this);
        Tv_forgotPassword = findViewById(R.id.Tv_forgotPassword);
        Tv_forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:
                changeButtonBackground(btn_login_login, null);
                if (App.getSinltonPojo().getForgotPasswordStatus() != null) {
                    if (App.getSinltonPojo().getForgotPasswordStatus().equalsIgnoreCase("1")) {
                        validate();
                    } else {
                        validate();
                    }
                } else {
                    validate();
                }
                break;
            case R.id.Tv_forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_fb_login:
                FBLogin();
                break;
            case R.id.img_google_login:
                SignIn();
                break;
        }
    }

    private void validate() {
        S_userEmail = email_Login.getText().toString();
        S_userPassword = password_login.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}";

        if (S_userEmail.isEmpty()){
            email_Login.setError("Enter email");
        }else if (S_userEmail.length()<10){
            email_Login.setError("Enter valid email");
        }
        else if (S_userPassword.isEmpty()) {
            password_login.setError("enter minimum 7 character password");
        } else {
            login();
        }
    }

    private void login() {
        String reg_id = FirebaseInstanceId.getInstance().getToken();
        String device_type = "Android";
        viewModel.loginUser(activity, S_userEmail, S_userPassword, device_type, reg_id).observe(LoginActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                    App.getAppPreference().saveLoginDetail(loginRegisterModel);
                    App.getAppPreference().saveString(AppConstants.TOKEN, "1");
                    if (App.getSinltonPojo().getForgotPasswordStatus() != null) {
                        if (App.getSinltonPojo().getForgotPasswordStatus().equalsIgnoreCase("1")) {
//                            startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
                            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                        } else if (App.getSinltonPojo().getForgotPasswordStatus().equalsIgnoreCase("0")) {
                            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                        }
                    } else {
                        startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                    }
                }else if (loginRegisterModel.getSuccess().equalsIgnoreCase("2")){

//                    startActivity(new Intent(LoginActivity.this, OTPVerificationActivity.class));

                    App.getSinltonPojo().setRegisterClass(loginRegisterModel);
                    App.getSinltonPojo().setRegisterStatus("login");
                    Intent intent = new Intent(LoginActivity.this, OTPVerificationActivity.class);
                    intent.putExtra("notVerified","1");
                    intent.putExtra("id",loginRegisterModel.getDetails().getId());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void FBLogin() {
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "");
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    CommonUtils.dismissProgress();

                    Log.d("onSuccess: ", loginResult.getAccessToken().getToken());
                    getFacebookData(loginResult);
//                    Toast.makeText(activity, ""+userStringEmail, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancel() {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    CommonUtils.dismissProgress();
                    if (error instanceof FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                    FBLogin();
                }
            });
        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFacebookData(LoginResult loginResult) {
        CommonUtils.showProgress(activity, "");
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                CommonUtils.dismissProgress();
                try {

                    if (object.has("id")) {
                        fbId = object.getString("id");
                        Log.e("LoginActivity", "id" + fbId);

                    }
                    //check permission first userName
                    if (object.has("first_name")) {
                        fbFirstName = object.getString("first_name");
                        Log.e("LoginActivity", "first_name" + fbFirstName);

                    }
                    //check permisson last userName
                    if (object.has("last_name")) {
                        fbLastName = object.getString("last_name");
                        Log.e("LoginActivity", "last_name" + fbLastName);
                    }
                    //check permisson email
                    if (object.has("email")) {
                        fbEmail = object.getString("email");
                        Log.e("LoginActivity", "email" + fbEmail);
                    }
                    if (object.has("phoneNumber")) {
                        fbPhoneNumber = object.getString("phoneNumber");
                        Log.e("LoginActivity", "email" + fbPhoneNumber);
                    }

                    if (object.has("gender")) {
                        fbGender = object.getString("gender");
                        Log.e("LoginActivity", "email" + fbGender);
                    }

                    if (object.has("dateofbirth")) {
                        fbDateOfBirth = object.getString("dateofbirth");
                        Log.e("LoginActivity", "email" + fbDateOfBirth);
                    }

                    if (object.has("country")) {
                        fbCountry = object.getString("country");
                        Log.e("LoginActivity", "email" + fbCountry);
                    }
                    JSONObject jsonObject = new JSONObject(object.getString("picture"));
                    if (jsonObject != null) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        Log.e("LoginActivity", "json oject get picture" + dataObject);
                        fbProfilePicturenew = new URL("https://graph.facebook.com/" + fbId + "/picture?width=500&height=500");
                        Log.e("LoginActivity", "json object=>" + object.toString());
                    }

                    fbSocialUserName = fbFirstName + " " + fbLastName;

                    userName = fbSocialUserName;
                    userStringEmail = fbEmail;
                    socialId = fbId;
                    phoneNumber = fbPhoneNumber;
                    gender = fbGender;
                    dateofbirth = fbDateOfBirth;
                    country = fbCountry;
                    loginType = "facebook";

                    if (fbProfilePicture != null) {
                        userImage = String.valueOf(fbProfilePicturenew);
                    } else {
                        userImage = "";
                    }

                    CheckSocialId();

                } catch (Exception e) {
                }
            }
        });

        Bundle bundle = new Bundle();
        Log.e("LoginActivity", "bundle set");
        bundle.putString("fields", "id, first_name, last_name,email,picture,gender,location");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    private void CheckSocialId() {
        viewModel.checkSocialId(activity, socialId).observe(LoginActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                    App.getAppPreference().saveLoginDetail(loginRegisterModel);
                    App.getAppPreference().saveString(AppConstants.TOKEN, "1");
                    startActivity(new Intent(activity, HomePageActivity.class));
                    finishAffinity();
                } else {
                    socialLogin();
                }
            }
        });
    }

    private void socialLogin() {
        String reg_id = FirebaseInstanceId.getInstance().getToken();
        String device_type = "Android";

        viewModel.socialLogin(activity, userName, userStringEmail, phoneNumber,userImage, socialId, device_type, reg_id, loginType).observe(LoginActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                    App.getAppPreference().saveLoginDetail(loginRegisterModel);
                    App.getAppPreference().saveString(AppConstants.TOKEN, "1");
                    startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                    finishAffinity();

                } else {
                    Toast.makeText(activity, loginRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void SignIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        CommonUtils.showProgress(activity, "");
        if (result.isSuccess()) {
            CommonUtils.dismissProgress();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Account: ", acct.getDisplayName());
            Log.d("Account: ", acct.getId());
            Log.d("Account: ", acct.getEmail());
            socialId = acct.getId();
            userName = acct.getDisplayName();
            userStringEmail = acct.getEmail();
            loginType = "Google";
            if (acct.getPhotoUrl() != null) {
                userImage = String.valueOf(acct.getPhotoUrl());
            } else {
                userImage = "";
            }
            App.getAppPreference().saveString(AppConstants.LOGIN_TYPE, "Google");
            CheckSocialId();
        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            CommonUtils.dismissProgress();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult task = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(task);
        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        unChange = new Button[]{btn_login_login};
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
