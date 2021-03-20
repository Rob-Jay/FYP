package com.example.fyp;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.FileInputStream;


public class SummarizeActivity extends AppCompatActivity {
    TextView textView;
    TextView sTextView;
    Button clearTextBtn;
    Button scanTextBtn;
    Button summarizeBtn;
    Button saveSummaryBtn;
    ImageView ScanTextImg;
    String originalText;
    private static String TAG = "SUMMARIZEACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summarization);
        //Entities from XML file located
        Log.d(TAG,"Summarize activity is running");
        textView = findViewById(R.id.text_view);
        sTextView = findViewById(R.id.summary_text_view);
        clearTextBtn = findViewById(R.id.clear_text_btn);
        scanTextBtn = findViewById(R.id.add_text_btn);
        summarizeBtn = findViewById(R.id.summarize_btn);
        saveSummaryBtn = findViewById(R.id.save_summary_btn);
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

                            textView.append(s);
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


    }
//    protected void onSaveInstanceState(@NonNull Bundle outState ) {
//        super.onSaveInstanceState(outState);
//        Log.d(TAG,"On saved instance state");
//        CharSequence text_view =textView.getText();
//        CharSequence stext_view =sTextView.getText();
//        outState.putCharSequence("textView",text_view);
//        outState.putCharSequence("stextView",stext_view);
//
//
//    }
//
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState ) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Log.d(TAG,"On restore instance state");
//        CharSequence store_text_view = savedInstanceState.getCharSequence("textView");
//        CharSequence store_Summary_text_view = savedInstanceState.getCharSequence("stextView");
//        Log.d(TAG,"text View has been read");
//
//        textView.setText(store_text_view);
//        sTextView.setText(store_Summary_text_view);
//
//
//    }

    public void ScanText(View view) {
        //open the camera => create an Intent object
        Intent cameraActivityIntent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(cameraActivityIntent);
        finish();


        Intent intent=new Intent(this,CameraActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public void clearText(View view) {
        textView.setText("");
        sTextView.setText("");



    }

}

