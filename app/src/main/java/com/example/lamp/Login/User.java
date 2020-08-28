
package com.example.lamp.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifiedAt;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("secret_code")
    @Expose
    private Object secretCode;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("interested")
    @Expose
    private Object interested;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Object getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(Object secretCode) {
        this.secretCode = secretCode;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Object getInterested() {
        return interested;
    }

    public void setInterested(Object interested) {
        this.interested = interested;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
