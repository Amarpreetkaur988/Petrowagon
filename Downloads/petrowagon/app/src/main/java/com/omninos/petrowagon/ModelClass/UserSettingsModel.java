package com.omninos.petrowagon.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSettingsModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Details details;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public class Details {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("sms")
        @Expose
        private String sms;
        @SerializedName("productEmails")
        @Expose
        private String productEmails;
        @SerializedName("marketingEmails")
        @Expose
        private String marketingEmails;
        @SerializedName("nstatus")
        @Expose
        private String nstatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSms() {
            return sms;
        }

        public void setSms(String sms) {
            this.sms = sms;
        }

        public String getProductEmails() {
            return productEmails;
        }

        public void setProductEmails(String productEmails) {
            this.productEmails = productEmails;
        }

        public String getMarketingEmails() {
            return marketingEmails;
        }

        public void setMarketingEmails(String marketingEmails) {
            this.marketingEmails = marketingEmails;
        }

        public String getNstatus() {
            return nstatus;
        }

        public void setNstatus(String nstatus) {
            this.nstatus = nstatus;
        }

    }
}
