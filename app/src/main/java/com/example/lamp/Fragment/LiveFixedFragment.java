package com.example.lamp.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamp.Activity.BeforeHomeActivity;
import com.example.lamp.Activity.UserInterfaceContainerActivity;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LiveFixedFragment extends Fragment {
    private Toolbar dToolbar;
    private Context context;
    private String type = "live_fixed", retrievedToken;
    private EditText title, slug, description, stock, unitPrice, unit, agentId, category, startedAt, expiredAt;
    private Button uploadProductBtn;
    private ImageView oneImage, twoImage, threeImage;
    protected static TextView viewDate1, viewDate2;
    private SharedPreferences preferences;
    private Intent oneIntent, twoIntent, threeIntent;
    private File oneFile, twoFile, threeFile;
    public static final String TAG = "Live";
    private Uri oneImageUri, twoImageUri, threeImageUri;

    public LiveFixedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_fixed, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
        dToolbar.setTitle(getString(R.string.live_fixed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        //final FragmentManager fm = getChildFragmentManager();

        /////////ImagePicker//////////
        oneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseOneImage();
            }
        });
        twoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChooseTwoImage();
            }
        });
        threeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChooseThreeImage();
            }
        });
        /////////ImagePicker//////////

        /*/////////datePicker//////////
        startedAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new DatePickerFragment1();

                newFragment.show(fm, "datePicker");
            }
        });
        expiredAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new DatePickerFragment2();

                newFragment.show(fm, "datePicker");
            }
        });
        /////////datePicker//////////*/

        /*uploadProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreProduct();
            }
        });*/

    }



    private void ChooseOneImage() {
        oneIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(oneIntent,1);
    }

   /* private void ChooseTwoImage() {
        twoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(twoIntent,2);
    }

    private void ChooseThreeImage() {
        threeIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(threeIntent,3);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: "+"Activity result success");
        super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == 1){
                    if(resultCode == -1 && data != null && data.getData() != null) {
                        oneImageUri = data.getData();
                        Log.d(TAG, "onActivityResult: "+oneImageUri);
                        String OneImageFilePath = getRealPathFromURI(oneImageUri);
                        Log.d(TAG, "onActivityResult: "+ OneImageFilePath);
                        File f1 = new File(OneImageFilePath);
                        String FineName1 = f1.getName();
                        Log.d(TAG, "onActivityResult: "+ FineName1);
                        Picasso.get().load(oneImageUri).into(oneImage);

                    }
        }
      /*  if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
                twoImageUri = data.getData();
                String TwoImageFilePath = getRealPathFromURI(twoImageUri);
                Log.d(TAG, "onActivityResult: "+ TwoImageFilePath);
                File f2 = new File(TwoImageFilePath);
                String FineName2 = f2.getName();
                Log.d(TAG, "onActivityResult: "+ FineName2);
                Picasso.get().load(twoImageUri).into(twoImage);
            }
        }*/



                /*if(requestCode == 3) {
                    if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
                        threeImageUri = data.getData();
                        String ThreeImageFilePath = getRealPathFromURI(threeImageUri);
                        Log.d(TAG, "onActivityResult: "+ ThreeImageFilePath);
                        File f3 = new File(ThreeImageFilePath);
                        String FineName3 = f3.getName();
                        Log.d(TAG, "onActivityResult: "+ FineName3);
                        Picasso.get().load(threeImageUri).into(threeImage);
                    }
                }
                    */
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

  /*  private void StoreProduct() {

        String pTitle = title.getText().toString().trim();
        String pDescription = description.getText().toString().trim();
        String pSlug = slug.getText().toString().trim();
        String pStock = stock.getText().toString().trim();
        String pUnitPrice = unitPrice.getText().toString().trim();
        String pUnit = unit.getText().toString().trim();
        String pAgentId = agentId.getText().toString().trim();
        String pCategory = category.getText().toString().trim();
        String pStartAt = viewDate1.getText().toString().trim();
        String pExpireAt = viewDate2.getText().toString().trim();

        oneFile = new File(getRealPathFromUri(oneImageUri, context));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), oneFile);
        Log.d(TAG, "onClick: "+requestBody.toString());

        twoFile = new File(getRealPathFromUri(twoImageUri, context));
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), twoFile);

        threeFile = new File(getRealPathFromUri(threeImageUri, context));
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), threeFile);


        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);


            Call<Datum> call = api.postByProductStoreQuery("Bearer " + retrievedToken,
                    pTitle, pSlug, pDescription, requestBody, requestBody1, requestBody2,
                    type, pUnitPrice, pUnit, pStock, pAgentId, pCategory, pStartAt, pExpireAt);

            call.enqueue(new Callback<Datum>() {
                @Override
                public void onResponse(Call<Datum> call, Response<Datum> response) {
                    Log.d(TAG, "onResponse: response code: " +response.code());

                    if(response.code() == 200){
                        Toast.makeText(context, "Successfully Added your Product", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, UserInterfaceContainerActivity.class);
                        context.startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Datum> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+"message: "+ t.getMessage());
                }
            });
        }

    }*/
        /*String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
        cursor.close();
        return result;*/


    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = view.findViewById(R.id.toolbar);
        }
        title = view.findViewById(R.id.tvLiveProductTitle);
        slug = view.findViewById(R.id.tvLiveProductSlug);
        description = view.findViewById(R.id.tvLiveProductDescription);
        stock = view.findViewById(R.id.tvLiveProductStock);
        unitPrice = view.findViewById(R.id.tvLiveProductPrice);
        unit = view.findViewById(R.id.tvLiveProductUnit);
        agentId = view.findViewById(R.id.tvLiveAgentId);
        category = view.findViewById(R.id.tvLiveProductCategory);
        startedAt = view.findViewById(R.id.startDateET);
        expiredAt = view.findViewById(R.id.endDateET);

        oneImage = view.findViewById(R.id.image1);
        twoImage = view.findViewById(R.id.image2);
        threeImage = view.findViewById(R.id.image3);

        uploadProductBtn = view.findViewById(R.id.uploadLiveProductBtn);
    }

    /*//DatePickerMethods
    @SuppressLint("ValidFragment")
    public static class DatePickerFragment1 extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            viewDate1 = getActivity().findViewById(R.id.startDateET);
           *//* int actualMonth = month+1; // Because month index start from zero
            // Display the unformatted date to TextView
            tvDate.setText("Year : " + year + ", Month : " + actualMonth + ", Day : " + day + "\n\n");*//*

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style medium and UK locale
            DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String df_medium_uk_str = df_medium_uk.format(chosenDate);
            // Display the formatted date
            viewDate1.setText(df_medium_uk_str);
        }
    }
    //End of DatePickerMethods

    //DatePickerMethods
    @SuppressLint("ValidFragment")
    public static class DatePickerFragment2 extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            viewDate2 = getActivity().findViewById(R.id.endDateET);
           *//* int actualMonth = month+1; // Because month index start from zero
            // Display the unformatted date to TextView
            tvDate.setText("Year : " + year + ", Month : " + actualMonth + ", Day : " + day + "\n\n");*//*

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style medium and UK locale
            DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String df_medium_uk_str = df_medium_uk.format(chosenDate);
            // Display the formatted date
            viewDate2.setText(df_medium_uk_str);
        }
    }
    //End of DatePickerMethods*/
}