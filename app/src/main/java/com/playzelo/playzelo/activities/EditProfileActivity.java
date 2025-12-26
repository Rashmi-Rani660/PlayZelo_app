package com.playzelo.playzelo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.databinding.ActivityEditProfileBinding;

import java.io.IOException;
import java.util.Objects;

public class EditProfileActivity extends BaseActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    private static final int REQUEST_PERMISSIONS = 300;
    private ActivityEditProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPermissions();

        String savedName = getSharedPreferences("UserProfile", MODE_PRIVATE)
                .getString("displayName", "");
        binding.displayName.setText(savedName);


        binding.openCameraButton.setOnClickListener(v -> openCamera());
        binding.selectGalleryButton.setOnClickListener(v -> openGallery());
        binding.selectAvatarButton.setOnClickListener(v -> selectAvatar());

        binding.displayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString().trim();
                if (!name.isEmpty() && name.matches("^[a-zA-Z ]+$")) {
                    binding.saveChanges.setEnabled(true);
                    binding.saveChanges.setBackgroundTintList(ContextCompat.getColorStateList(EditProfileActivity.this, android.R.color.holo_purple));
                } else {
                    binding.saveChanges.setEnabled(false);
                    binding.saveChanges.setBackgroundTintList(ContextCompat.getColorStateList(EditProfileActivity.this, android.R.color.white));
                    Log.d("DEBUG", "SaveChanges Enabled: " + binding.saveChanges.isEnabled());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.saveChanges.setOnClickListener(v -> {
            String name = binding.displayName.getText().toString().trim();

            if (!name.isEmpty() && name.matches("^[a-zA-Z ]+$")) {
                // Yaha par aap changes ko save kar sakte ho
                // Example: SharedPreferences me save karna
                getSharedPreferences("UserProfile", MODE_PRIVATE)
                        .edit()
                        .putString("displayName", name)
                        .apply();

                // Toast ya Snackbar dikhana
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                // Activity close karke previous screen pe jao
                finish();
            } else {
                Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @SuppressLint("IntentReset")
    private void openGallery() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void selectAvatar() {
        // You can load a predefined avatar from drawable
        binding.profileImage.setImageResource(R.drawable.ic_avtar); // Replace with any avatar drawable
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                binding.profileImage.setImageBitmap(photo);
            } else if (requestCode == REQUEST_GALLERY) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    binding.profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.e("Error: ", Objects.requireNonNull(e.getLocalizedMessage()));
                }
            }
        }
    }
}
