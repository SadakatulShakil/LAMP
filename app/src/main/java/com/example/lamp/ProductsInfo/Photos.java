
package com.example.lamp.ProductsInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photos implements Serializable {

    @SerializedName("one")
    @Expose
    private String one;
    @SerializedName("two")
    @Expose
    private String two;
    @SerializedName("three")
    @Expose
    private String three;

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

}
