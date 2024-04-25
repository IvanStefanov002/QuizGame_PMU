package com.example.loginregister.EntryModule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregister.Models.UserSingleton;
import com.example.loginregister.R;
import com.example.loginregister.Utilities.DesignFunctionalities;
import com.example.loginregister.Utilities.Requests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends Activity {
    UserSingleton sObject = UserSingleton.getInstance();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    TextView tvUsername, tvEmail, tvPoints;
    ImageView profileImageView;
    Button changePictureButton;
    final String sImagePath = "/data/user/0/com.example.loginregister/app_profile_images/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DesignFunctionalities.hideSystemUI(this);

        //Initializing variables
        profileImageView = findViewById(R.id.profile_image);
        changePictureButton = findViewById(R.id.change_picture_button);
        tvUsername = findViewById(R.id.username_tv);
        tvEmail = findViewById(R.id.email_tv);
        tvPoints = findViewById(R.id.points_tv);

        //setting Username, Email and Points text views from SingletonObject
        tvUsername.setText("Псевдоним: | " + sObject.getUsername());
        tvEmail.setText("Имейл: | " +sObject.getEmail());
        tvPoints.setText("Точки: | " + sObject.getPoints());

        //setting the ImageView to user's picture from DataBase
        String sImageName = Requests.getUserImageNameFromDB(sObject.getUsername());
        if(!sImageName.isEmpty()){ //set image only if DB return image name
            Bitmap bitmap = BitmapFactory.decodeFile((sImagePath + sImageName).toString());
            profileImageView.setImageBitmap(bitmap);
        }

        //setting onClickListener for *Change Image* button
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                startCameraIntent();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraIntent();
            } else {
                Toast.makeText(this, "Достъп до камерата отказан!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(imageBitmap);

            // Save the image to internal storage
            saveImageToInternalStorage(imageBitmap);
        } else {
            Toast.makeText(this, "Неуспеншно заснемане на снимка", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean saveImageToInternalStorage(Bitmap bitmap) {
        // Create directory if not exists
        File directory = getDir("profile_images", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            //if directory profile_images doesn't exist -> create it
            directory.mkdirs();
        }

        // Save image with a unique name
        String imageName = "profile_image_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(directory, imageName);
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Set the Bitmap to your ImageView
            profileImageView.setImageBitmap(bitmap);
            Requests.updateUserImageInDB(sObject.getUsername(), imageName);

            //display Toast message to the user
            Toast.makeText(this, "Снимката е запазена!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Неуспешно запазване на снимка!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}