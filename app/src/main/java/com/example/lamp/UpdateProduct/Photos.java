
package com.example.lamp.UpdateProduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos {

    @SerializedName("one")
    @Expose
    private String one;
    @SerializedName("one_thumb")
    @Expose
    private String oneThumb;
    @SerializedName("two")
    @Expose
    private String two;
    @SerializedName("two_thumb")
    @Expose
    private String twoThumb;
    @SerializedName("three")
    @Expose
    private String three;
    @SerializedName("three_thumb")
    @Expose
    private String threeThumb;

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getOneThumb() {
        return oneThumb;
    }

    public void setOneThumb(String oneThumb) {
        this.oneThumb = oneThumb;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getTwoThumb() {
        return twoThumb;
    }

    public void setTwoThumb(String twoThumb) {
        this.twoThumb = twoThumb;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getThreeThumb() {
        return threeThumb;
    }

    public void setThreeThumb(String threeThumb) {
        this.threeThumb = threeThumb;
    }

}
