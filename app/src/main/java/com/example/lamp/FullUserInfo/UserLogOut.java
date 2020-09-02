package com.example.lamp.FullUserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserLogOut implements Serializable {


    private String name;

    public UserLogOut() {
    }

    public UserLogOut(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserLogOut{" +
                "name='" + name + '\'' +
                '}';
    }
}
