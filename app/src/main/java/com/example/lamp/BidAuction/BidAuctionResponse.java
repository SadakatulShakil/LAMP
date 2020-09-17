
package com.example.lamp.BidAuction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BidAuctionResponse implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("from")
    @Expose
    private Object from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("min_bid_price")
    @Expose
    private Double minBidPrice;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unit_charge")
    @Expose
    private Integer unitCharge;
    @SerializedName("total_charge")
    @Expose
    private Integer totalCharge;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getMinBidPrice() {
        return minBidPrice;
    }

    public void setMinBidPrice(Double minBidPrice) {
        this.minBidPrice = minBidPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getUnitCharge() {
        return unitCharge;
    }

    public void setUnitCharge(Integer unitCharge) {
        this.unitCharge = unitCharge;
    }

    public Integer getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Integer totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
