
package com.example.lamp.OrderStore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payments implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("amount")
    @Expose
    private Object amount;
    @SerializedName("method")
    @Expose
    private Object method;
    @SerializedName("date")
    @Expose
    private Object date;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public Object getMethod() {
        return method;
    }

    public void setMethod(Object method) {
        this.method = method;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

}
