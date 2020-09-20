package com.example.lamp.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.example.lamp.Activity.UserInterfaceContainerActivity;
import com.example.lamp.Adapter.CategoryAdapter;
import com.example.lamp.Adapter.UnitAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.Model.CategoryType;
import com.example.lamp.Model.UnitType;
import com.example.lamp.R;
import com.example.lamp.UploadInfo.UploadInfo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LiveAuctionFragment extends Fragment {
    private Toolbar dToolbar;
    private Context context;
    private String retrievedToken, productUnit, productCategory;
    private EditText title, slug, description, stock, unitPrice, agentId, startedDate, expiredDate, minimumBidPrice;
    private Button uploadProductBtn;
    private ImageView oneImage, twoImage, threeImage;
    private SharedPreferences preferences;
    private Intent oneIntent, twoIntent, threeIntent;
    private File oneFile, twoFile, threeFile;
    public static final String TAG = "PreBook";
    private Uri oneImageUri, twoImageUri, threeImageUri;
    private ProgressBar progressBar;
    private Spinner unitSpinner, categorySpinner;
    private ArrayList<UnitType> mProductUnitList;
    private ArrayList<CategoryType> mProductCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private UnitAdapter mUnitAdapter;

    public LiveAuctionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_auction, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        inItUnitList();
        inItCategoryList();

        initView(view);
        preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
        dToolbar.setTitle(getString(R.string.prebook_fixed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

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
                ChooseTwoImage();
            }
        });
        threeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseThreeImage();
            }
        });
        /////////ImagePicker//////////

        /////////datePicker//////////
        startedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sampleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                                startedDate.setText(sampleDateFormat.format(calendar.getTime()));
                                Log.d(TAG, "Start Time: "+startedDate);
                            }
                        };
                        new TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                    }
                };
                new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        expiredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sampleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                                expiredDate.setText(sampleDateFormat.format(calendar.getTime()));
                                Log.d(TAG, "End Time: "+expiredDate);
                            }
                        };
                        new TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                    }
                };
                new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        /////////datePicker//////////

        uploadProductBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                progressBar.setVisibility(View.VISIBLE);
                StoreProduct();
            }
        });

    }
    private void inItCategoryList() {
        mProductCategoryList = new ArrayList<>();
        mProductCategoryList.add(new CategoryType("Select Category..."));
        mProductCategoryList.add(new CategoryType("ফল"));
        mProductCategoryList.add(new CategoryType("পোলট্রি"));
        mProductCategoryList.add(new CategoryType("সবজি"));

    }

    private void inItUnitList() {
        mProductUnitList = new ArrayList<>();
        mProductUnitList.add(new UnitType("Select Unit..."));
        mProductUnitList.add(new UnitType("কেজি"));
        mProductUnitList.add(new UnitType("মণ"));
        mProductUnitList.add(new UnitType("ডজন"));

    }


    private void ChooseOneImage() {
        oneIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(oneIntent, 1);
    }

    private void ChooseTwoImage() {
        twoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(twoIntent, 2);
    }

    private void ChooseThreeImage() {
        threeIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(threeIntent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: " + "Activity result success");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == -1 && data != null && data.getData() != null) {
                oneImageUri = data.getData();
                Log.d(TAG, "onActivityResult: " + oneImageUri);
                Picasso.get().load(oneImageUri).into(oneImage);

            }
        } else if (requestCode == 2) {
            if (resultCode == -1 && data != null && data.getData() != null) {
                twoImageUri = data.getData();
                Log.d(TAG, "onActivityResult: " + twoImageUri);
                Picasso.get().load(twoImageUri).into(twoImage);
            }
        } else if (requestCode == 3) {
            if (resultCode == -1 && data != null && data.getData() != null) {
                threeImageUri = data.getData();
                Log.d(TAG, "onActivityResult: " + threeImageUri);
                Picasso.get().load(threeImageUri).into(threeImage);
            }
        }
    }

    private String getRealPathFromUri(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void StoreProduct() {
        String type = "live_auction";
        String pTitle = title.getText().toString().trim();
        String pDescription = description.getText().toString().trim();
        String pSlug = slug.getText().toString().trim();
        int pStock = Integer.parseInt(stock.getText().toString().trim());
        double pUnitPrice = Double.parseDouble(unitPrice.getText().toString().trim());
        String pUnit = productUnit;
        String pAgentId = agentId.getText().toString().trim();
        String pCategory = productCategory;
        String pStartAt = startedDate.getText().toString().trim();
        String pExpireAt = expiredDate.getText().toString().trim();
        String mBidPrice = String.valueOf((pUnitPrice*pStock));
        minimumBidPrice.setText(mBidPrice);

        oneFile = new File(getRealPathFromUri(oneImageUri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), oneFile);
        Log.d(TAG, "onClick: " + requestBody.toString());

        twoFile = new File(getRealPathFromUri(twoImageUri));
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), twoFile);

        threeFile = new File(getRealPathFromUri(threeImageUri));
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), threeFile);


        if (retrievedToken != null) {
            Retrofit retrofit = RetrofitClient.getRetrofitClient();
            ApiInterface api = retrofit.create(ApiInterface.class);


            Call<UploadInfo> call = api.postByProductStoreQuery("Bearer " + retrievedToken,
                    pTitle, pSlug, pDescription, requestBody, requestBody1, requestBody2, type,
                    pExpireAt, pStartAt, pStock, pUnitPrice, pUnit, pAgentId, pCategory);

            call.enqueue(new Callback<UploadInfo>() {
                @Override
                public void onResponse(Call<UploadInfo> call, Response<UploadInfo> response) {
                    Log.d(TAG, "onResponse: response code: " + response.code());

                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Successfully Added your Product", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, UserInterfaceContainerActivity.class);
                        context.startActivity(intent);
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<UploadInfo> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }

    }

    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = view.findViewById(R.id.toolbar);
        }
        title = view.findViewById(R.id.tvLiveProductTitle);
        slug = view.findViewById(R.id.tvLiveProductSlug);
        description = view.findViewById(R.id.tvLiveProductDescription);
        stock = view.findViewById(R.id.tvLiveProductStock);
        unitPrice = view.findViewById(R.id.tvLiveProductPrice);
        unitSpinner = view.findViewById(R.id.spinnerUnitMeasure);
        categorySpinner = view.findViewById(R.id.spinnerCategory);
        agentId = view.findViewById(R.id.tvLiveAgentId);
        startedDate = view.findViewById(R.id.startDateET);
        expiredDate = view.findViewById(R.id.endDateET);

        oneImage = view.findViewById(R.id.image1);
        twoImage = view.findViewById(R.id.image2);
        threeImage = view.findViewById(R.id.image3);
        progressBar = view.findViewById(R.id.progressBar);
        minimumBidPrice = view.findViewById(R.id.tvLiveMinimumBidPrice);

        uploadProductBtn = view.findViewById(R.id.uploadLiveProductBtn);

        mUnitAdapter = new UnitAdapter(context, mProductUnitList);
        mCategoryAdapter = new CategoryAdapter(context, mProductCategoryList);

        unitSpinner.setAdapter(mUnitAdapter);
        categorySpinner.setAdapter(mCategoryAdapter);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UnitType clickedUnit = (UnitType) parent.getItemAtPosition(position);

                productUnit = clickedUnit.getUnit();

                Toast.makeText(context, productUnit +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryType clickedCategory = (CategoryType) parent.getItemAtPosition(position);

                productCategory = clickedCategory.getCategory();

                Toast.makeText(context, productCategory +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}