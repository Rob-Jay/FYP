package com.example.fyp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SummarizeActivity extends AppCompatActivity {
    TextView textView;
    TextView sTextView;
    TextView clearScanTextBtn;
    LinearLayout scanTextBtn;
    TextView clearSumTextBtn;
    TextView summarizeBtn;
    LinearLayout saveSummaryBtn;
    NumberPicker numeric_input;
    LinearLayout toolbarBack;
    private static final String TAG = "SUMMARIZEACTIVITY";
    private static final String KEY_SUMMARY_TEXT = "SUMMARYTEXT";
    private static final String KEY_SCAN_TEXT = "SCANTEXT";
    private static final String baseURL = "http://192.168.0.13:5000";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summarization);
        //Entities from XML file located
        Log.d(TAG, "Summarize activity is running");
        textView = findViewById(R.id.text_view);
        sTextView = findViewById(R.id.summary_text_view);
        clearScanTextBtn = findViewById(R.id.clear_text_btn);
        clearSumTextBtn = findViewById(R.id.clearSumTextBtn);
        toolbarBack = findViewById(R.id.toolbar_back);

        scanTextBtn = findViewById(R.id.add_text_btn);
        summarizeBtn = findViewById(R.id.summarize_btn);
        saveSummaryBtn = findViewById(R.id.save_summary_btn);

        Log.d(TAG, "Retrofit is running");

        //Connect to API
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                back(view);
            }
        });

        clearSumTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearSumText(view);
            }
        });
        clearScanTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearScanText(view);
            }
        });

        summarizeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Summarize(view);
            }
        });
        saveSummaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                save_summary(view);
            }
        });
        scanTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ScanText(view);
            }
        });


        //Calling posts method from Requests class
        //User numeric input with min and max value
        numeric_input = findViewById(R.id.number_picker);
        numeric_input.setMinValue(0);
        numeric_input.setMaxValue(25);
        //Getting information from OCR activity
        Bitmap bitmap = null;
        String filename = getIntent().getStringExtra("image");


        Log.d(TAG, "Before Try block ");
        try {
            String scan_text = getIntent().getStringExtra(KEY_SCAN_TEXT);
            String summary_text = getIntent().getStringExtra(KEY_SUMMARY_TEXT);
            sTextView.append(summary_text);
            textView.append(scan_text);
        } catch (Exception e) {
            Log.d(TAG, "Catch block has been reached line");
            e.printStackTrace();
        }
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            //1. create a FirebaseVisionImage object from a Bitmap
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            Log.d(TAG, "After FirebaseVisionImage Try block ");
            //2. Get an instance of FirebaseVision using a cloudTextRecognizer
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getCloudTextRecognizer();
            Log.d(TAG, "FirebaseVisionTextRecognizer has been instantiated ");
            //3. Create a task to process the image
            Task<FirebaseVisionText> result =
                    detector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            Log.d(TAG, "Task onSuccess has been passed ");

                            String s = firebaseVisionText.getText();
                            textView.append(s);
                        }
                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Task has failed " + e.getMessage());
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
        } catch (Exception e) {
            Log.d(TAG, "Catch block has been reached ");
            e.printStackTrace();
        }
    }


    public void ScanText(View view) {
        //open the camera => create an Intent object
        String scan_text = textView.getText().toString();
        String summary_text = sTextView.getText().toString();
        Intent cameraActivityIntent = new Intent(getApplicationContext(), CameraActivity.class);
        cameraActivityIntent.putExtra(KEY_SUMMARY_TEXT, scan_text);
        cameraActivityIntent.putExtra(KEY_SCAN_TEXT, summary_text);
        Log.d(TAG, "Intent is being passed");
        startActivity(cameraActivityIntent);
        finish();
    }

    private void clearSumText(View view) {
        Log.d(TAG, "Clear text has been pressed");
        if (sTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Your text has already been cleared", Toast.LENGTH_LONG).show();
        } else {
            sTextView.setText("");
        }
    }

    private void clearScanText(View view) {
        Log.d(TAG, "Clear text has been pressed");
        if (textView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Your text has already been cleared", Toast.LENGTH_LONG).show();
        } else {
            textView.setText("");
        }
    }

    private void Summarize(View view) {
        Log.d(TAG, "Summarize method is running");
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d(TAG, "Summarize method is running after retrofit and being passed baseurl");
            Placeholder placeholder = retrofit.create(Placeholder.class);
            Requests post = new Requests(numeric_input.getValue(), textView.getText().toString());
            Log.d(TAG, "Post request has been made containing:\t" + numeric_input.getValue() + "\t" + textView.getText().toString());
            Log.d(TAG, "Request has been made:\t" + post);
            Call<Requests> call = placeholder.createPost(post);
            Log.d(TAG, "Call has been made:\t" + call);
            call.enqueue(new Callback<Requests>() {
                @Override
                public void onResponse(Call<Requests> call, Response<Requests> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Response is unsuccessful");
                    }
                    Log.d(TAG, "Response is successful");
                    Requests postResponse = response.body();
                    String content = "";
                    content += postResponse.getSummarised_text();
                    sTextView.setText(content);
                }

                @Override
                public void onFailure(Call<Requests> call, Throwable t) {
                    Log.d(TAG, "Call has failed:\t" + t);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "Error:\t" + e);
        }

    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


    public void save_summary(View view) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (sTextView.getText().toString().length() < 5) {
            Toast.makeText(this, "You do not have enough text to save your Summary", Toast.LENGTH_LONG).show();
        } else {
            try {
                //Create a temporary file of the summarised text
                String data = sTextView.getText().toString();
                String filename = "";
                if (data.length() < 7) {
                    filename = data + "Summary";
                } else {
                    filename = data.substring(1, 9);
                }
                //Making an object of the summary
                Summary summary = new Summary(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        filename,
                        data
                        );
                db.collection("Documents").document().set(summary)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(getApplicationContext(), ViewFilesActivity.class);
                                startActivity(intent);
                                finish();                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error:\t" + e);
                            }
                        });
            } catch (Exception e) {
                Log.d(TAG, "Error:\t" + e);
                e.printStackTrace();
            }
        }

    }
}


