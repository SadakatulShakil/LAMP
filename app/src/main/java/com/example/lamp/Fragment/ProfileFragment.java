package com.example.lamp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Activity.FarmerActivity;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.FullUserInfo.UpdateUserInfo;
import com.example.lamp.MainActivity;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ProfileFragment extends Fragment {

    private Context context;
    private TextView name, email, contact, type, location, zip, city, country, logOutBtn, updateUser;
    private CircleImageView proImage;
    private ImageView nidImage;
    private String retrievedToken;
    private SharedPreferences preferences;
    public static final String TAG = "profile";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inItView(view);
        preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);

        getAuthUserData();

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogOut();
            }
        });
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getAuthUserData() {

        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);

            Call<UpdateUserInfo> call = api.getByAuthQuery("Bearer " + retrievedToken);
            call.enqueue(new Callback<UpdateUserInfo>() {
                @Override
                public void onResponse(Call<UpdateUserInfo> call, Response<UpdateUserInfo> response) {
                    if (response.code() == 200) {
                        UpdateUserInfo updateUserInfo = response.body();

                        name.setText(updateUserInfo.getName());
                        email.setText(updateUserInfo.getEmail());
                        contact.setText(updateUserInfo.getPhone());
                        type.setText(updateUserInfo.getType());
                        location.setText(updateUserInfo.getAddress().getLocation());
                        zip.setText(updateUserInfo.getAddress().getZip());
                        city.setText(updateUserInfo.getAddress().getCity());
                        country.setText(updateUserInfo.getAddress().getCountry());

                        Picasso.get().load(updateUserInfo.getPhoto()).into(proImage);
                        Picasso.get().load(updateUserInfo.getNid()).into(nidImage);

                    }
                }

                @Override
                public void onFailure(Call<UpdateUserInfo> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }
    }

    private void userLogOut() {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();

        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<String> call = api.postByLogOutQuery("Bearer " + retrievedToken);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() == 200) {
                    /*UserLogOut userLogOut = response.body();*/
                    preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    preferences.edit().putString("TOKEN", null).apply();
                    Log.d(TAG, "onResponse: " + response.body());
                    Intent intent1 = new Intent(context, MainActivity.class);
                    context.startActivity(intent1);
                    getActivity().finish();
                    Toast.makeText(context, "response: " + response.body(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void inItView(View view) {
        name = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.userEmail);
        contact = view.findViewById(R.id.userContact);
        type = view.findViewById(R.id.userType);
        location = view.findViewById(R.id.userLocation);
        zip = view.findViewById(R.id.userZip);
        city = view.findViewById(R.id.userCity);
        country = view.findViewById(R.id.userCountry);
        proImage = view.findViewById(R.id.profileImage);
        nidImage = view.findViewById(R.id.nidImage);
        logOutBtn = view.findViewById(R.id.logOutTv);
        updateUser = view.findViewById(R.id.userEdit);

    }
}