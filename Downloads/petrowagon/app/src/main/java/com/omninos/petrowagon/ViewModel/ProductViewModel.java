package com.omninos.petrowagon.ViewModel;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omninos.petrowagon.ModelClass.CheckVersionModel;
import com.omninos.petrowagon.ModelClass.ProductListModel;
import com.omninos.petrowagon.Retrofit.Api;
import com.omninos.petrowagon.Retrofit.ApiClient;
import com.omninos.petrowagon.SharePreference.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<ProductListModel> productListData;
    public LiveData<ProductListModel> productList(final Activity activity){
        productListData = new MutableLiveData<>();
//        CommonUtils.showProgress(activity,"");
        if (CommonUtils.isNetworkConnected(activity)){
            Api api = ApiClient.getClient().create(Api.class);
            api.productList().enqueue(new Callback<ProductListModel>() {
                @Override
                public void onResponse(Call<ProductListModel> call, Response<ProductListModel> response) {
                    if (response.body()!=null){
//                    CommonUtils.dismissProgress();
                        productListData.setValue(response.body());
                    }else {
                        ProductListModel productListModel=new ProductListModel();
                        productListModel.setSuccess("0");
                        productListModel.setMessage("Server Error");
                        productListData.setValue(productListModel);
                    }
                }

                @Override
                public void onFailure(Call<ProductListModel> call, Throwable t) {
//                CommonUtils.dismissProgress();
                    Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            ProductListModel productListModel=new ProductListModel();
            productListModel.setSuccess("0");
            productListModel.setMessage("Server Error");
            productListData.setValue(productListModel);
        }

        return  productListData;
    }


    private MutableLiveData<CheckVersionModel> checkVersionData;
    public LiveData<CheckVersionModel> checkVersion(final  Activity  activity){
        checkVersionData = new MutableLiveData<>();
        Api api = ApiClient.getClient().create(Api.class);
        api.getVersion().enqueue(new Callback<CheckVersionModel>() {
            @Override
            public void onResponse(Call<CheckVersionModel> call, Response<CheckVersionModel> response) {
                if (response.body()!=null){
                    checkVersionData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CheckVersionModel> call, Throwable t) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return  checkVersionData;
    }


}
