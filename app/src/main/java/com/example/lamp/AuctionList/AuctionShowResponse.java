
package com.example.lamp.AuctionList;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionShowResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("links")
    @Expose
    private Links links;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
