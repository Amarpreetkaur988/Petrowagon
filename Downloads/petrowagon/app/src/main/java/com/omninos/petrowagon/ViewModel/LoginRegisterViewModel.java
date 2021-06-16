package com.omninos.petrowagon.ViewModel;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omninos.petrowagon.ModelClass.ChangePasswordModel;
import com.omninos.petrowagon.ModelClass.ForgotPasswordModel;
import com.omninos.petrowagon.ModelClass.LoginRegisterModel;
import com.omninos.petrowagon.ModelClass.VarificationTokenModel;
import com.omninos.petrowagon.Retrofit.Api;
import com.omninos.petrowagon.Retrofit.ApiClient;
import com.omninos.petrowagon.SharePreference.CommonUtils;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegisterViewModel extends ViewModel {

    // Register
    private MutableLiveData<LoginRegisterModel> registerData;
    public LiveData<LoginRegisterModel> registerUser(final Activity activity, String name, String phone, String email, String password, final String reg_id, String device_type, String login_type){
        registerData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.userRegister(name,phone,email,password,reg_id,device_type,login_type).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){

                    registerData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return registerData;
    }

    // Login
    private MutableLiveData<LoginRegisterModel> loginData;
    public LiveData<LoginRegisterModel> loginUser (final Activity activity,  String userEmail, String password, String device_type, String reg_id){
        loginData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.userLogin(userEmail,password, device_type, reg_id).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){

                    loginData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        return loginData;
    }


    // Match Varification Token
    private MutableLiveData<VarificationTokenModel> tokenData;
    public LiveData<VarificationTokenModel> token(final Activity activity, String userId, String token){
        tokenData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.varificationToken(userId,token).enqueue(new Callback<VarificationTokenModel>() {
            @Override
            public void onResponse(Call<VarificationTokenModel> call, Response<VarificationTokenModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    tokenData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<VarificationTokenModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return  tokenData;
    }


    // Resend OTP

    private MutableLiveData<Map> resendOTPData;
    public LiveData<Map> resendOtp(final  Activity activity, String id){
        resendOTPData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.resendOTP(id).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){

                    resendOTPData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return resendOTPData;
    }

    // Social Login
    private MutableLiveData<LoginRegisterModel> socialLoginData;
    public LiveData<LoginRegisterModel> socialLogin(final Activity activity, String username, String email, String phone, String image, String social_id, String device_type, String reg_id, String loginType){
        socialLoginData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.userSocialLogin(username,email,phone,image, social_id,device_type,reg_id,loginType).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    socialLoginData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        return  socialLoginData;

    }

    // Check Social Id
    private MutableLiveData<LoginRegisterModel> checkSocialIdData;
    public LiveData<LoginRegisterModel>  checkSocialId (final  Activity activity, String socialId){
        checkSocialIdData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.checkSocialId(socialId).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    checkSocialIdData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return  checkSocialIdData;
    }

    // Edit Profile
    private MutableLiveData<LoginRegisterModel> editProfileData;
    public LiveData<LoginRegisterModel>  editProfile(final Activity activity, RequestBody userId, RequestBody name, RequestBody phone, RequestBody email, RequestBody address, final MultipartBody.Part image){
        editProfileData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.editProfile(userId,name,email,phone,address,image).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){

                    editProfileData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        return  editProfileData;
    }


    //Forgot Password
    private MutableLiveData<ForgotPasswordModel> forgotPasswordData;
    public LiveData<ForgotPasswordModel> forgotPassword (final Activity activity, String email){
        forgotPasswordData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.forgotPassword(email).enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    forgotPasswordData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return  forgotPasswordData;
    }

    // Change Password
    private MutableLiveData<ChangePasswordModel> changePasswordData;
    public LiveData<ChangePasswordModel> changePassword(final Activity activity, String userId, String currentPassword, String newPassword){
        changePasswordData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.changePassword(userId,currentPassword,newPassword).enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){

                    changePasswordData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        return changePasswordData;
    }

}
