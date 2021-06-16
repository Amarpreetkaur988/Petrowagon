package com.omninos.petrowagon.ViewModel;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.omninos.petrowagon.ModelClass.CancelOrderModel;
import com.omninos.petrowagon.ModelClass.ChargesModel;
import com.omninos.petrowagon.ModelClass.CheckDistanceModel;
import com.omninos.petrowagon.ModelClass.CurrentOrderModel;
import com.omninos.petrowagon.ModelClass.MinimumValueModel;
import com.omninos.petrowagon.ModelClass.PlaceOrderModel;
import com.omninos.petrowagon.ModelClass.RejectedOrderModel;
import com.omninos.petrowagon.ModelClass.ReorderProductModel;
import com.omninos.petrowagon.ModelClass.TrackOrderModel;
import com.omninos.petrowagon.ModelClass.TransactionModel;
import com.omninos.petrowagon.Retrofit.Api;
import com.omninos.petrowagon.Retrofit.ApiClient;
import com.omninos.petrowagon.SharePreference.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private MutableLiveData<PlaceOrderModel> placeOrderData;

    public LiveData<PlaceOrderModel> placeOrder(final Activity activity, String userId, String productId, String quantity, String status, String location, String date, String time, String pricePerlitre, String totalPrice, String paymentMethod, String token, String latitude, String longitude, String transactionId) {
        placeOrderData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.bookingOrder(userId, productId, quantity, status, location, date, time, pricePerlitre, totalPrice, paymentMethod, token, latitude, longitude, transactionId).enqueue(new Callback<PlaceOrderModel>() {
            @Override
            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    placeOrderData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return placeOrderData;
    }

    //
    private MutableLiveData<CurrentOrderModel> currentOrderData;

    public LiveData<CurrentOrderModel> currentOrder(final Activity activity, String userId) {
        currentOrderData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.currentOrderList(userId).enqueue(new Callback<CurrentOrderModel>() {
            @Override
            public void onResponse(Call<CurrentOrderModel> call, Response<CurrentOrderModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    CommonUtils.dismissProgress();
                    currentOrderData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<CurrentOrderModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return currentOrderData;
    }

    //
    private MutableLiveData<CurrentOrderModel> pastOrderData;

    public LiveData<CurrentOrderModel> pastOrder(final Activity activity, String userId) {
        pastOrderData = new MutableLiveData<>();
//        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.pastOrderList(userId).enqueue(new Callback<CurrentOrderModel>() {
            @Override
            public void onResponse(Call<CurrentOrderModel> call, Response<CurrentOrderModel> response) {
//                CommonUtils.dismissProgress();
                if (response.body() != null) {
//                    CommonUtils.dismissProgress();
                    pastOrderData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CurrentOrderModel> call, Throwable t) {
//                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return pastOrderData;
    }

    // Reorder Product
    private MutableLiveData<ReorderProductModel> reorderProductData;

    public LiveData<ReorderProductModel> reorderProduct(final Activity activity, String userId, String orderId, String date, String time, String location, String paymentMethod, String latitude, String longitude, String transactionId) {
        reorderProductData = new MutableLiveData<>();
//        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.reorderProduct(userId, orderId, date, time, location, paymentMethod, latitude, longitude, transactionId).enqueue(new Callback<ReorderProductModel>() {
            @Override
            public void onResponse(Call<ReorderProductModel> call, Response<ReorderProductModel> response) {
                if (response.body() != null) {
//                    CommonUtils.dismissProgress();
                    reorderProductData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReorderProductModel> call, Throwable t) {
//                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return reorderProductData;
    }


    // Track Order
    private MutableLiveData<TrackOrderModel> trackOrderData;

    public LiveData<TrackOrderModel> trackOrder(final Activity activity, String orderId) {
        trackOrderData = new MutableLiveData<>();
        Api api = ApiClient.getClient().create(Api.class);
        api.trackOrderList(orderId).enqueue(new Callback<TrackOrderModel>() {
            @Override
            public void onResponse(Call<TrackOrderModel> call, Response<TrackOrderModel> response) {
                if (response.body() != null) {
                    trackOrderData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TrackOrderModel> call, Throwable t) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return trackOrderData;
    }


    // Track Latest Order
    private MutableLiveData<TrackOrderModel> trackLatestOrderData;

    public LiveData<TrackOrderModel> trackLatestOrder(final Activity activity, String userId) {
        trackOrderData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.trackLatestOrderList(userId).enqueue(new Callback<TrackOrderModel>() {
            @Override
            public void onResponse(Call<TrackOrderModel> call, Response<TrackOrderModel> response) {
                if (response.body() != null) {
                    CommonUtils.dismissProgress();
                    trackOrderData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TrackOrderModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        return trackOrderData;
    }

    // Transaction List
    private MutableLiveData<TransactionModel> transactionListData;

    public LiveData<TransactionModel> transactionList(final Activity activity, String userId) {
        transactionListData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.getTransactionList(userId).enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    transactionListData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return transactionListData;
    }


    // Cancel Order

    private MutableLiveData<CancelOrderModel> cancelOrderData;
    public LiveData<CancelOrderModel> cancelOrder(final Activity activity, String orderId) {
        cancelOrderData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.cancelOrder(orderId).enqueue(new Callback<CancelOrderModel>() {
            @Override
            public void onResponse(Call<CancelOrderModel> call, Response<CancelOrderModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    cancelOrderData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CancelOrderModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return cancelOrderData;
    }

    // Check Distance
    private MutableLiveData<CheckDistanceModel> checkDistanceData;
    public LiveData<CheckDistanceModel> checkDistance(final Activity activity, String lat, String lng, String userId, String address){
        checkDistanceData = new MutableLiveData<>();
//        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.checkDistance(lat, lng, userId, address).enqueue(new Callback<CheckDistanceModel>() {
            @Override
            public void onResponse(Call<CheckDistanceModel> call, Response<CheckDistanceModel> response) {
//                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    checkDistanceData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CheckDistanceModel> call, Throwable t) {
//                CommonUtils.dismissProgress();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return checkDistanceData;
    }

    // Charges List

    private MutableLiveData<ChargesModel> chargesListData;
    public LiveData<ChargesModel> chargesList (final Activity activity){
        chargesListData = new MutableLiveData<>();
        CommonUtils.showProgress(activity,"");
        Api api = ApiClient.getClient().create(Api.class);
        api.getCharges().enqueue(new Callback<ChargesModel>() {
            @Override
            public void onResponse(Call<ChargesModel> call, Response<ChargesModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    chargesListData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ChargesModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return chargesListData;
    }


    // Rejected Order List

    private MutableLiveData<RejectedOrderModel> rejectedOrderData;
    public  LiveData<RejectedOrderModel> rejectedOrder (final  Activity activity, String userId){
        rejectedOrderData = new MutableLiveData<>();
        CommonUtils.showProgress(activity, "");
        Api api = ApiClient.getClient().create(Api.class);
        api.rejectedOrderList(userId).enqueue(new Callback<RejectedOrderModel>() {
            @Override
            public void onResponse(Call<RejectedOrderModel> call, Response<RejectedOrderModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    rejectedOrderData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<RejectedOrderModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        return  rejectedOrderData;
    }


    // Minimum Value

    private MutableLiveData<MinimumValueModel> minimumData;
    public LiveData<MinimumValueModel> minimum(final Activity activity){
        minimumData = new MutableLiveData<>();
        Api api = ApiClient.getClient().create(Api.class);
        CommonUtils.showProgress(activity,"");
        api.getMinimumValue().enqueue(new Callback<MinimumValueModel>() {
            @Override
            public void onResponse(Call<MinimumValueModel> call, Response<MinimumValueModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    minimumData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MinimumValueModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        return minimumData;
    }




}
