package com.example.lamp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lamp.Adapter.CategoryAdapter;
import com.example.lamp.Adapter.UnitAdapter;
import com.example.lamp.Adapter.productTypeAdapter;
import com.example.lamp.Adapter.productsAdapter;
import com.example.lamp.Api.ApiInterface;
import com.example.lamp.Api.RetrofitClient;
import com.example.lamp.Model.CategoryType;
import com.example.lamp.Model.UnitType;
import com.example.lamp.Model.productType;
import com.example.lamp.ProductsInfo.Datum;
import com.example.lamp.ProductsInfo.ProductsInfo;
import com.example.lamp.R;
import com.example.lamp.UpdateProduct.UpdateProduct;
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

public class ProductUpdateActivity extends AppCompatActivity {
    private EditText title, slug, description, stock, unitPrice, agentId, startedDate, expiredDate;
    private ImageView oneImage, twoImage, threeImage;
    private Spinner unitSpinner, categorySpinner,productTypeSpinner;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private String retrievedToken, productUnit, productCategory,productType;
    private ArrayList<UnitType> mProductUnitList;
    private ArrayList<CategoryType> mProductCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private UnitAdapter mUnitAdapter;
    private ArrayList<productType> mProductTypeList;
    private productTypeAdapter mAdapter;
    private Intent oneIntent, twoIntent, threeIntent;
    private File oneFile, twoFile, threeFile;
    private Uri oneImageUri, twoImageUri, threeImageUri;
    private Button uploadProductBtn;
    private Datum productData;
    private Toolbar dToolbar;

    public static final String TAG = "UpdateProduct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);
        inItUnitList();
        inItCategoryList();
        inItProductTypeList();
        inItView();
        dToolbar.setTitle(getString(R.string.update));
        Intent intent = getIntent();
        productData = (Datum) intent.getSerializableExtra("productData");

        showCurrentData();
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken = preferences.getString("TOKEN", null);
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
                        new TimePickerDialog(ProductUpdateActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                    }
                };
                new DatePickerDialog(ProductUpdateActivity.this, dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                        new TimePickerDialog(ProductUpdateActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                    }
                };
                new DatePickerDialog(ProductUpdateActivity.this, dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
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


    private void showCurrentData() {

        title.setText(productData.getTitle());
        slug.setText(productData.getSlug());
        description.setText(productData.getDescription());
        stock.setText(String.valueOf(productData.getStock()));
        unitPrice.setText(String.valueOf(productData.getUnitPrice()));
        agentId.setText(productData.getAgentId());
        startedDate.setText(productData.getStartedAt());
        expiredDate.setText(productData.getExpiredAt());
        Picasso.get().load(productData.getPhotos().getOne()).into(oneImage);
        Picasso.get().load(productData.getPhotos().getTwo()).into(twoImage);
        Picasso.get().load(productData.getPhotos().getThree()).into(threeImage);
    }

    /*private void getProductsData() {
        Log.d(TAG, "getProductsData: started");
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<ProductsInfo> call = api.getByProductsQuery("Bearer " + retrievedToken);

        call.enqueue(new Callback<ProductsInfo>() {
            @Override
            public void onResponse(Call<ProductsInfo> call, Response<ProductsInfo> response) {
                Log.d(TAG, "onResponse: "+ response.code());
                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    ProductsInfo productsData = response.body();


                }

            }

            @Override
            public void onFailure(Call<ProductsInfo> call, Throwable t) {
                Toast.makeText(ProductUpdateActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });

    }*/

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

    private void inItProductTypeList() {
        mProductTypeList = new ArrayList<>();
        mProductTypeList.add(new productType("Select Product Type..."));
        mProductTypeList.add(new productType("live_fixed"));
        mProductTypeList.add(new productType("prebook_fixed"));
        mProductTypeList.add(new productType("live_auction"));
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
        CursorLoader loader = new CursorLoader(ProductUpdateActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void StoreProduct() {
        String type = productType;
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


            Call<UpdateProduct> call = api.postByProductUpdateQuery("Bearer " + retrievedToken, productData.getId(),
                    pTitle, pSlug, pDescription, requestBody, requestBody1, requestBody2, type,
                    pExpireAt, pStartAt, pStock, pUnitPrice, pUnit, pAgentId, pCategory);

            call.enqueue(new Callback<UpdateProduct>() {
                @Override
                public void onResponse(Call<UpdateProduct> call, Response<UpdateProduct> response) {
                    Log.d(TAG, "onResponse: response code: " + response.code());

                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProductUpdateActivity.this, "Successfully Updated your Product", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProductUpdateActivity.this, UserInterfaceContainerActivity.class);
                        ProductUpdateActivity.this.startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UpdateProduct> call, Throwable t) {
                    Toast.makeText(ProductUpdateActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + "message: " + t.getMessage());
                }
            });
        }

    }

    private void inItView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dToolbar = findViewById(R.id.toolbar);
        }
        title = findViewById(R.id.tvLiveProductTitle);
        slug = findViewById(R.id.tvLiveProductSlug);
        description = findViewById(R.id.tvLiveProductDescription);
        stock = findViewById(R.id.tvLiveProductStock);
        unitPrice = findViewById(R.id.tvLiveProductPrice);
        unitSpinner = findViewById(R.id.spinnerUnitMeasure);
        categorySpinner = findViewById(R.id.spinnerCategory);
        productTypeSpinner = findViewById(R.id.spinnerProductType);
        agentId = findViewById(R.id.tvLiveAgentId);
        startedDate = findViewById(R.id.startDateET);
        expiredDate = findViewById(R.id.endDateET);
        uploadProductBtn = findViewById(R.id.uploadLiveProductBtn);

        oneImage = findViewById(R.id.image1);
        twoImage = findViewById(R.id.image2);
        threeImage = findViewById(R.id.image3);
        progressBar = findViewById(R.id.progressBar);


        mUnitAdapter = new UnitAdapter(ProductUpdateActivity.this, mProductUnitList);
        mCategoryAdapter = new CategoryAdapter(ProductUpdateActivity.this, mProductCategoryList);

        unitSpinner.setAdapter(mUnitAdapter);
        categorySpinner.setAdapter(mCategoryAdapter);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UnitType clickedUnit = (UnitType) parent.getItemAtPosition(position);

                productUnit = clickedUnit.getUnit();

                Toast.makeText(ProductUpdateActivity.this, productUnit +" is selected !", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(ProductUpdateActivity.this, productCategory +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdapter = new productTypeAdapter(ProductUpdateActivity.this, mProductTypeList);

        productTypeSpinner.setAdapter(mAdapter);

        productTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productType clickedType = (productType) parent.getItemAtPosition(position);

                productType = clickedType.getProductType();

                Toast.makeText(ProductUpdateActivity.this, productType +" is selected !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}