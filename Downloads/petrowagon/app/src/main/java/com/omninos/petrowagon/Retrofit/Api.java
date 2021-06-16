package com.omninos.petrowagon.Retrofit;

import com.omninos.petrowagon.ModelClass.CancelOrderModel;
import com.omninos.petrowagon.ModelClass.ChangePasswordModel;
import com.omninos.petrowagon.ModelClass.ChargesModel;
import com.omninos.petrowagon.ModelClass.CheckDistanceModel;
import com.omninos.petrowagon.ModelClass.CheckVersionModel;
import com.omninos.petrowagon.ModelClass.CurrentOrderModel;
import com.omninos.petrowagon.ModelClass.DeliveryTimeModel;
import com.omninos.petrowagon.ModelClass.DescriptionModel;
import com.omninos.petrowagon.ModelClass.FAQModel;
import com.omninos.petrowagon.ModelClass.ForgotPasswordModel;
import com.omninos.petrowagon.ModelClass.LoginRegisterModel;
import com.omninos.petrowagon.ModelClass.MinimumValueModel;
import com.omninos.petrowagon.ModelClass.NotificationModel;
import com.omninos.petrowagon.ModelClass.PartnerListModel;
import com.omninos.petrowagon.ModelClass.PlaceOrderModel;
import com.omninos.petrowagon.ModelClass.ProductListModel;
import com.omninos.petrowagon.ModelClass.RejectedOrderModel;
import com.omninos.petrowagon.ModelClass.ReorderProductModel;
import com.omninos.petrowagon.ModelClass.TrackOrderModel;
import com.omninos.petrowagon.ModelClass.TransactionModel;
import com.omninos.petrowagon.ModelClass.UserSettingsModel;
import com.omninos.petrowagon.ModelClass.VarificationTokenModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


    @FormUrlEncoded
    @POST("userRegister")
    Call<LoginRegisterModel> userRegister(@Field("name") String name,
                                          @Field("phone") String phone,
                                          @Field("email") String email,
                                          @Field("password") String password,
                                          @Field("reg_id") String reg_id,
                                          @Field("device_type") String device_type,
                                          @Field("login_type") String login_type);

    @FormUrlEncoded
    @POST("userLogin")
    Call<LoginRegisterModel> userLogin(@Field("email") String userEmail,
                                       @Field("password") String password,
                                       @Field("device_type") String device_type,
                                       @Field("reg_id") String reg_id);

    @FormUrlEncoded
    @POST("matchVerificationToken")
    Call<VarificationTokenModel> varificationToken(@Field("id") String userId,
                                                   @Field("token") String token);

    @FormUrlEncoded
    @POST("resendVerificationToken")
    Call<Map> resendOTP(@Field("id") String id);
//
//
//    @Multipart
//    @POST("userUpdateProfile")
//    Call<LoginRegisterModel> editProfile(@Part("name") RequestBody name,
//                                         @Part MultipartBody.Part qrCode,
//                                         @Part("gender") RequestBody gender,
//                                         @Part("phone") RequestBody phone,
//                                         @Part("dob") RequestBody dob,
//                                         @Part("address") RequestBody address,
//                                         @Part("aboutMe") RequestBody aboutMe,
//                                         @Part("height") RequestBody height,
//                                         @Part("userId") RequestBody userId);
//
//
//    @FormUrlEncoded
//    @POST("changePassword")
//    Call<ChangePasswordModel> changePassword(@Field("userId") String userId,
//                                             @Field("oldPassword") String currentPassword,
//                                             @Field("newPassword") String newPassword);
//
//
//    @FormUrlEncoded
//    @POST("changeEmail")
//    Call<ChangeEmailModel> changeEmail(@Field("userId") String userId,
//                                       @Field("email") String email,
//                                       @Field("newEmail") String newemail);
//
//    @FormUrlEncoded
//    @POST("forgotPassword")
//    Call<ForgotPasswordModel> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("UserSocialLogin")
    Call<LoginRegisterModel> userSocialLogin(@Field("username") String username,
                                             @Field("email") String email,
                                             @Field("phone") String phone,
                                             @Field("image") String image,
                                             @Field("social_id") String social_id,
                                             @Field("device_type") String device_type,
                                             @Field("reg_id") String reg_id,
                                             @Field("loginType") String loginType);


    @FormUrlEncoded
    @POST("userCheckSocialId")
    Call<LoginRegisterModel> checkSocialId(@Field("social_id") String social_id);


    @GET("getProductList")
    Call<ProductListModel> productList();

    @Multipart
    @POST("editUserProfile")
    Call<LoginRegisterModel> editProfile(@Part("userId") RequestBody userIds,
                                         @Part("name") RequestBody username,
                                         @Part("email") RequestBody email,
                                         @Part("phone") RequestBody phone,
                                         @Part("address") RequestBody address,
                                         @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("userPlaceOrder")
    Call<PlaceOrderModel> bookingOrder(@Field("userId") String userId,
                                       @Field("productId") String productId,
                                       @Field("quantity") String quantity,
                                       @Field("status") String status,
                                       @Field("location") String location,
                                       @Field("date") String date,
                                       @Field("time") String time,
                                       @Field("pricePerLitre") String pricePerlitre,
                                       @Field("totalPrice") String totalPrice,
                                       @Field("paymentMethod") String paymentMethod,
                                       @Field("token") String token,
                                       @Field("latitude") String latitude,
                                       @Field("longitude") String longitude,
                                       @Field("transactionId") String transactionId);


    @FormUrlEncoded
    @POST("getCurrentOrderList")
    Call<CurrentOrderModel> currentOrderList(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("getPastOrderList")
    Call<CurrentOrderModel> pastOrderList(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("getOrderRejectList")
    Call<RejectedOrderModel> rejectedOrderList(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("userReorderProduct")
    Call<ReorderProductModel> reorderProduct(@Field("userId") String userId,
                                             @Field("orderId") String orderId,
                                             @Field("date") String date,
                                             @Field("time") String time,
                                             @Field("location") String location,
                                             @Field("paymentMethod") String paymentMethod,
                                             @Field("latitude") String latitude,
                                             @Field("longitude") String longitude,
                                             @Field("transactionId") String transactionId);

    @FormUrlEncoded
    @POST("getTrackOrderList")
    Call<TrackOrderModel> trackOrderList(@Field("bookingOrderId") String orderId);


    @FormUrlEncoded
    @POST("getLatestOrderList")
    Call<TrackOrderModel> trackLatestOrderList(@Field("userId") String userId);


    @GET("getPartnerList")
    Call<PartnerListModel> partnerList();

//    @GET("getPartnerList")
//    Call<AboutFAQModel> aboutUsList();

    @GET("getFaqList")
    Call<FAQModel> faq();

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ForgotPasswordModel> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("changePassword")
    Call<ChangePasswordModel> changePassword(@Field("userId") String userId,
                                             @Field("old_password") String currentPassword,
                                             @Field("new_password") String newPassword);

    @FormUrlEncoded
    @POST("pushNotificationsOnOff")
    Call<NotificationModel> pushNotification(@Field("userId") String userId,
                                             @Field("status") String status);


    @FormUrlEncoded
    @POST("getTransactionList")
    Call<TransactionModel> getTransactionList(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("cancelOrder")
    Call<CancelOrderModel> cancelOrder(@Field("orderId") String orderId);

    @FormUrlEncoded
    @POST("checkDestance")
    Call<CheckDistanceModel> checkDistance(@Field("lat") String lat,
                                           @Field("long") String lng,
                                           @Field("userId") String userId,
                                           @Field("address") String address);


    @FormUrlEncoded
    @POST("getUserSettings")
    Call<UserSettingsModel> getSttings(@Field("userId") String userId);


    @FormUrlEncoded
    @POST("userChangeAlert")
    Call<Map> changeSetting(@Field("productEmails") String productEmails,
                            @Field("marketingEmails") String marketingEmails,
                            @Field("userId") String userId);

    @GET("getCharges")
    Call<ChargesModel> getCharges();


    @GET("getProductListNew")
    Call<DescriptionModel> getDescription();

    @GET("getDeliveryTime")
    Call<DeliveryTimeModel> getDeliveryTime();

    @GET("getMinimumValues")
    Call<MinimumValueModel> getMinimumValue();

    @GET("checkVersion")
    Call<CheckVersionModel> getVersion();

}
