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
import android.provider.MediaStore;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Bitmap bitmap;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Main Activity is running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get User Information
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d("tag","onCreate" +firebaseAuth.getCurrentUser().getEmail() + firebaseAuth.getCurrentUser().getDisplayName());
        logOutButton =findViewById(R.id.button);

        //find imageview
        imageView = findViewById(R.id.imageId);
        //find textview
        textView = findViewById(R.id.textId);
        Bitmap bitmap = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            //1. create a FirebaseVisionImage object from a Bitmap
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            //2. Get an instance of FirebaseVision using a cloudTextRecognizer
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getCloudTextRecognizer();
            //3. Create a task to process the image
            Task<FirebaseVisionText> result =
                    detector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String s = firebaseVisionText.getText();
                            textView.setText(s);
                        }
                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });





        } catch (Exception e) {
            e.printStackTrace();
        }





        //check app level permission is granted for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }


    }

    public void doProcess(View view) {
        //open the camera => create an Intent object
        System.out.println("Button Clicked");
        System.out.println("Do Process runs");
        Log.d("tag","Camera is Pressed");


        Intent cameraActivityIntent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(cameraActivityIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //from bundle, extract the image
        //set image in imageview

    }

//Logging the user out
public void logout(final View view) {
    FirebaseAuth.getInstance().signOut();
    Log.d("tag","LogoutButton is Pressed");

    GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            startActivity(new Intent(view.getContext(),LoginActivity.class));
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this, "Signout Failed.", Toast.LENGTH_SHORT).show();
        }
    });
}
}