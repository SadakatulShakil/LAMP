
package com.example.lamp.FullUserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateUserInfo implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("email_verified_at")
    @Expose
    private Boolean emailVerifiedAt;
    @SerializedName("interested")
    @Expose
    private Object interested;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("token")
    @Expose
    private String token;

    public UpdateUserInfo() {
    }

    public UpdateUserInfo(String name, String phone, String email, String type, Address address, String photo, String nid, String token) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.address = address;
        this.photo = photo;
        this.nid = nid;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Boolean emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public Object getInterested() {
        return interested;
    }

    public void setInterested(Object interested) {
        this.interested = interested;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
