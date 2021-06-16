package com.omninos.petrowagon.ViewModel;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omninos.petrowagon.ModelClass.DeliveryTimeModel;
import com.omninos.petrowagon.ModelClass.DescriptionModel;
import com.omninos.petrowagon.ModelClass.FAQModel;
import com.omninos.petrowagon.ModelClass.PartnerListModel;
import com.omninos.petrowagon.Retrofit.Api;
import com.omninos.petrowagon.Retrofit.ApiClient;
import com.omninos.petrowagon.SharePreference.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionViewModel extends ViewModel {

    private MutableLiveData<PartnerListModel> partnerListData;
    public LiveData<PartnerListModel> partnerList (final Activity  activity){
        partnerListData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.partnerList().enqueue(new Callback<PartnerListModel>() {
            @Override
            public void onResponse(Call<PartnerListModel> call, Response<PartnerListModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){

                    partnerListData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PartnerListModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return  partnerListData;
    }

//    // About Us
//    private MutableLiveData<AboutFAQModel> aboutUsData;
//    public LiveData<AboutFAQModel> aboutUs(final Activity activity){
//        aboutUsData = new MutableLiveData<>();
//        CommonUtils.showProgress(activity, "");
//        Api api = ApiClient.getClient().create(Api.class);
//        api.aboutUsList().enqueue(new Callback<AboutFAQModel>() {
//            @Override
//            public void onResponse(Call<AboutFAQModel> call, Response<AboutFAQModel> response) {
//                CommonUtils.dismissProgress();
//                if (response.body()!=null){
//                    aboutUsData.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AboutFAQModel> call, Throwable t) {
//                CommonUtils.dismissProgress();
//                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return  aboutUsData;
//    }

    // FAQ/Help
    private MutableLiveData<FAQModel> faqData;
    public LiveData<FAQModel> faq(final Activity activity){
        faqData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.faq().enqueue(new Callback<FAQModel>() {
            @Override
            public void onResponse(Call<FAQModel> call, Response<FAQModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    faqData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FAQModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return  faqData;
    }

    //Description
    private MutableLiveData<DescriptionModel> descriptionData;
    public LiveData<DescriptionModel> description(final Activity activity){
        descriptionData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.getDescription().enqueue(new Callback<DescriptionModel>() {
            @Override
            public void onResponse(Call<DescriptionModel> call, Response<DescriptionModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    descriptionData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DescriptionModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error ", Toast.LENGTH_SHORT).show();
            }
        });
        return  descriptionData;
    }


    // Delivery Time

    private MutableLiveData<DeliveryTimeModel> deliveryTimeData;
    public LiveData<DeliveryTimeModel> deliveryTime (final Activity activity){
        deliveryTimeData = new MutableLiveData<>();
       // CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.getDeliveryTime().enqueue(new Callback<DeliveryTimeModel>() {
            @Override
            public void onResponse(Call<DeliveryTimeModel> call, Response<DeliveryTimeModel> response) {
               // CommonUtils.dismissProgress();
                if (response.body()!=null){
                    deliveryTimeData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DeliveryTimeModel> call, Throwable t) {
                //CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        return deliveryTimeData;
    }
}
