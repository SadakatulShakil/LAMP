package com.example.lamp.Api;

import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.FullUserInfo.UserLogOut;
import com.example.lamp.Login.UserLogin;
import com.example.lamp.ProductsInfo.ProductsInfo;
import com.example.lamp.Registration.UserRegistration;

import okhttp3.ResponseBody;
import retrofit2.Call;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @Multipart
    @POST("/api/mobile/auth-user/update")
    Call<UpdateUserInfo> postByUpdateInfo(
            @Header("Authorization") String token,
            @Query("type") String type,
            @Query("location") String location,
            @Query("city") String city,
            @Query("zip") String zip,
            @Query("country") String country,
            @Query("phone") String phone,
            @Query("email") String email,
            @Part("photo\"; filename=\"myProfile.jpg\" " ) RequestBody photoFile,
            @Part("nid\"; filename=\"myNid.jpg\" ") RequestBody nidFile,
            @Query("name") String name
    );

    @Headers("accept: application/json")
    @GET("api/mobile/auth-user")
    Call<UpdateUserInfo> getByAuthQuery(
            @Header("Authorization") String token
    );

    @Headers("accept: application/json")
    @POST("api/mobile/logout")
    Call<String> postByLogOutQuery(
            @Header("Authorization") String token
    );

    @Headers("accept: application/json")
    @GET("api/mobile/products")
    Call<ProductsInfo> getByProductsQuery(
            @Header("Authorization") String token
    );
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

