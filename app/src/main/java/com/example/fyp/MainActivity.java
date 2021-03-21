package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button logOutButton;
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = "SummarizeActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Get User Information
        logOutButton = findViewById(R.id.button);
        //find imageview
        imageView = findViewById(R.id.imageId);
        //find textview
        textView = findViewById(R.id.textId);

        //check app level permission is granted for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }


    //Checking if someone is logged in. If True stay in Main activity
    //else move back to Login
    //OnStart Called When Activity starts for the first time.
    public void onStart() {
        super.onStart();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //User is logged in
        if (mFirebaseUser != null) {
        } else {
            //No one is logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    //OnClickMethod to Open Camera activity from the take picture button
    public void doProcess(View view) {
        //open the camera => create an Intent object
        Intent cameraActivityIntent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(cameraActivityIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //from bundle, extract the image
        //set image in imageview
    }

    //Logging the user out by using get Instance.signout for regualr email
//GoogleSignIn.getClient if they are signed in with Gmail
    public void logout(final View view) {
        FirebaseAuth.getInstance().signOut();
        Log.d("tag", "LogoutButton is Pressed");

        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(view.getContext(), LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Signout Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //OnClickMethod to open SummarizeActivity
    public void summarize(View view) {
        startActivity(new Intent(getApplicationContext(), SummarizeActivity.class));
        finish();
    }
}