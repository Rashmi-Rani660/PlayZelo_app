package com.playzelo.playzelo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.playzelo.playzelo.databinding.ActivitySignUpBinding;
import com.playzelo.playzelo.models.SignUpResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;
    private ApiService apiService;

    private String selectedCountry = "Select Country";
    private String encodedImage = "";

    // Image Picker Launcher
    private final ActivityResultLauncher<Intent> pickImage =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            try {
                                assert imageUri != null;
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                binding.imgProfile.setImageBitmap(bitmap);
                                binding.txtAddImage.setVisibility(View.GONE);
                                encodedImage = encodeImage(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> onBackPressed());


        apiService = ApiClient.getClient().create(ApiService.class);

        setupCountrySpinner();
        setupDatePicker();
        setupImagePicker();
        setupRegisterButton();
    }

    private void setupCountrySpinner() {
        String[] countries = {
                "Select Country",
                "India",
                "Pakistan",
                "Sri Lanka",
                "Bangladesh"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCountry.setAdapter(adapter);

        binding.spCountry.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        selectedCountry = countries[position];
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }

    private void setupDatePicker() {
        binding.etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignUpActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String date = String.format(
                                Locale.getDefault(),
                                "%04d-%02d-%02d",
                                year,
                                month + 1,
                                dayOfMonth
                        );
                        binding.etDob.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupImagePicker() {
        binding.imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void setupRegisterButton() {
        binding.btnRegister.setOnClickListener(v -> {

            String username = binding.etUsername.getText().toString().trim();
            String dob = binding.etDob.getText().toString().trim();
            String address = binding.etAddress.getText().toString().trim();
            String city = binding.etCity.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (username.isEmpty() || dob.isEmpty() ||
                    selectedCountry.equals("Select Country") ||
                    address.isEmpty() || city.isEmpty() ||
                    email.isEmpty() || phone.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        SignUpActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            registerUser(
                    username,
                    dob,
                    selectedCountry,
                    address,
                    city,
                    email,
                    phone,
                    password,
                    encodedImage
            );
        });
    }

    // Convert Bitmap to Base64
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    private void registerUser(
            String username,
            String dob,
            String country,
            String address,
            String city,
            String email,
            String phone,
            String password,
            String profileImage
    ) {

        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("dob", dob);
        body.put("country", country);
        body.put("address", address);
        body.put("city", city);
        body.put("email", email);
        body.put("phone", phone);
        body.put("password", password);
        body.put("profileImage", profileImage);

        apiService.registerUser(body).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(
                    @NonNull Call<SignUpResponse> call,
                    @NonNull Response<SignUpResponse> response
            ) {

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(
                            SignUpActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                    // ✅ Redirect to Login Screen
                    Intent intent =
                            new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                    startActivity(intent);
                    finish();

                } else {
                    try {
                        String error =
                                response.errorBody() != null
                                        ? response.errorBody().string()
                                        : "Unknown error";
                        Log.e("SignUpAPI", error);
                        Toast.makeText(
                                SignUpActivity.this,
                                "Registration failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<SignUpResponse> call,
                    @NonNull Throwable t
            ) {
                Log.e("SignUpAPI", t.getMessage(), t);
                Toast.makeText(
                        SignUpActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
