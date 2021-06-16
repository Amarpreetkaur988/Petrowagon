package com.omninos.petrowagon.ViewModel;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omninos.petrowagon.ModelClass.NotificationModel;
import com.omninos.petrowagon.ModelClass.UserSettingsModel;
import com.omninos.petrowagon.Retrofit.Api;
import com.omninos.petrowagon.Retrofit.ApiClient;
import com.omninos.petrowagon.SharePreference.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsViewModel extends ViewModel {

    // Push Notification
    private MutableLiveData<NotificationModel> pushNotificationData;
    public LiveData<NotificationModel> pushnotification(final Activity activity, String userId, final String status){
        pushNotificationData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.pushNotification(userId, status).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.body()!=null){
                    CommonUtils.dismissProgress();
                    pushNotificationData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return pushNotificationData;
    }

    // Get Settings
    private MutableLiveData<UserSettingsModel> userSettingData;
    public LiveData<UserSettingsModel> userSetting(final Activity activity, String userId){
        userSettingData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.getSttings(userId).enqueue(new Callback<UserSettingsModel>() {
            @Override
            public void onResponse(Call<UserSettingsModel> call, Response<UserSettingsModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    userSettingData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserSettingsModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return  userSettingData;
    }

    // Change Setting
    private MutableLiveData<Map> changeSettingData;
    public LiveData<Map> changeSetting(final Activity activity, String productEmails,String marketingEmails,String userId){
        changeSettingData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.changeSetting(productEmails,marketingEmails,userId).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    changeSettingData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return  changeSettingData;
    }
}
