package com.omninos.petrowagon.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MinimumValueModel {


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

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

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }


    public class Detail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("minimumPrice")
        @Expose
        private String minimumPrice;
        @SerializedName("minimumQuantity")
        @Expose
        private String minimumQuantity;
        @SerializedName("priceText")
        @Expose
        private String priceText;
        @SerializedName("quantityText")
        @Expose
        private String quantityText;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMinimumPrice() {
            return minimumPrice;
        }

        public void setMinimumPrice(String minimumPrice) {
            this.minimumPrice = minimumPrice;
        }

        public String getMinimumQuantity() {
            return minimumQuantity;
        }

        public void setMinimumQuantity(String minimumQuantity) {
            this.minimumQuantity = minimumQuantity;
        }

        public String getPriceText() {
            return priceText;
        }

        public void setPriceText(String priceText) {
            this.priceText = priceText;
        }

        public String getQuantityText() {
            return quantityText;
        }

        public void setQuantityText(String quantityText) {
            this.quantityText = quantityText;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

    }

}
