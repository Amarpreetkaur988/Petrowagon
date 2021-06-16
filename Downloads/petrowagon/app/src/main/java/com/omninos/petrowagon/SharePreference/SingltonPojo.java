package com.omninos.petrowagon.SharePreference;

import com.omninos.petrowagon.ModelClass.LoginRegisterModel;

public class SingltonPojo {
    LoginRegisterModel RegisterClass;
    String resendOTP;
    String productId, productName, productPrice,productStatus, productType, productQuantity, productLocation, productDate,productTime, productDeliveryChargers, productTotalPrice, paymentMethod;
    String orderId, reorderStatus, trackOrderStatus;
    String forgotPasswordStatus;
    String oldPAssword;
    String stripeToken;
    String cancelOrderId;
    String mCity, mCountry, mZipCode, mState;
    String notificetionBookingId;
    String webViewStatus;
    String minPriceText, minQuantityText;
    String dropAddress;

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getMinPriceText() {
        return minPriceText;
    }

    public void setMinPriceText(String minPriceText) {
        this.minPriceText = minPriceText;
    }

    public String getMinQuantityText() {
        return minQuantityText;
    }

    public void setMinQuantityText(String minQuantityText) {
        this.minQuantityText = minQuantityText;
    }

    public String getWebViewStatus() {
        return webViewStatus;
    }

    public void setWebViewStatus(String webViewStatus) {
        this.webViewStatus = webViewStatus;
    }

    public String getNotificetionBookingId() {
        return notificetionBookingId;
    }

    public void setNotificetionBookingId(String notificetionBookingId) {
        this.notificetionBookingId = notificetionBookingId;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmZipCode() {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getCancelOrderId() {
        return cancelOrderId;
    }

    public void setCancelOrderId(String cancelOrderId) {
        this.cancelOrderId = cancelOrderId;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public String getOldPAssword() {
        return oldPAssword;
    }

    public void setOldPAssword(String oldPAssword) {
        this.oldPAssword = oldPAssword;
    }

    public String getForgotPasswordStatus() {
        return forgotPasswordStatus;
    }

    public void setForgotPasswordStatus(String forgotPasswordStatus) {
        this.forgotPasswordStatus = forgotPasswordStatus;
    }

    public String getTrackOrderStatus() {
        return trackOrderStatus;
    }

    public void setTrackOrderStatus(String trackOrderStatus) {
        this.trackOrderStatus = trackOrderStatus;
    }

    public String getReorderStatus() {
        return reorderStatus;
    }

    public void setReorderStatus(String reorderStatus) {
        this.reorderStatus = reorderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductDeliveryChargers() {
        return productDeliveryChargers;
    }

    public void setProductDeliveryChargers(String productDeliveryChargers) {
        this.productDeliveryChargers = productDeliveryChargers;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getResendOTP() {
        return resendOTP;
    }

    public void setResendOTP(String resendOTP) {
        this.resendOTP = resendOTP;
    }

    public LoginRegisterModel getRegisterClass() {
        return RegisterClass;
    }

    public void setRegisterClass(LoginRegisterModel registerClass) {
        RegisterClass = registerClass;
    }

   String currentLat, currentLong;

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(String currentLong) {
        this.currentLong = currentLong;
    }

    String registerStatus;

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
}
