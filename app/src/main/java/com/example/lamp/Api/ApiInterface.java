package com.example.lamp.Api;

import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.Login.UserLogin;
import com.example.lamp.Registration.UserRegistration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("accept: application/json, content-type: multipart/form-data")
    @POST("/api/mobile/register")
    Call<UserRegistration> postByRgQuery(
           @Query("name") String name,
           @Query("phone") String phone,
           @Query("email") String email,
           @Query("type") String type,
           @Query("password") String password,
           @Query("password_confirmation") String password_confirmation
    );

    @Headers("accept: application/json, content-type: multipart/form-data")
    @POST("/api/mobile/login")
    Call<UserLogin> postByLoginQuery(
            @Query("email") String email,
            @Query("password") String password,
            @Query("device_name") String device_name
    );

    @Headers("accept: application/json, content-type: multipart/form-data")
    @POST("/api/mobile/auth-user/update")
    Call<UpdateUserInfo> postByUpdateInfo(@Body UpdateUserInfo updateUserInfo);
/*
    @GET()
    Call<List<UserPost>> getByPostUrl(
            @Url String url
    );

    @GET()
    Call<List<CommentsResponse>> getByCommentsUrl(
            @Url String url
    );

    @GET()
    Call<List<PhotoAlbum>> getByAlbumsUrl(
            @Url String url
    );
    @GET()
    Call<List<UserPhoto>> getByPhotosUrl(
            @Url String url
    );*/

}

