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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SummarizeActivity extends AppCompatActivity {
    private TextView textView;
    private TextView sTextView;
    private TextView clearScanTextBtn;
    private LinearLayout scanTextBtn;
    private TextView clearSumTextBtn;
    private TextView summarizeBtn;
    private LinearLayout saveSummaryBtn;
    private NumberPicker numeric_input;
    private LinearLayout toolbarBack;
    private static final String TAG = "SUMMARIZEACTIVITY";
    private static final String KEY_SUMMARY_TEXT = "SUMMARYTEXT";
    private static final String KEY_SCAN_TEXT = "SCANTEXT";
    private static final String baseURL = "http://192.168.0.13:5000";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Placeholder placeholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summarization);
        //Entities from XML file located
        Log.d(TAG, "Summarize activity is running");

        //Connecting to XML
        textView = findViewById(R.id.text_view);
        sTextView = findViewById(R.id.summary_text_view);
        clearScanTextBtn = findViewById(R.id.clear_text_btn);
        clearSumTextBtn = findViewById(R.id.clearSumTextBtn);
        toolbarBack = findViewById(R.id.toolbar_back);
        scanTextBtn = findViewById(R.id.add_text_btn);
        summarizeBtn = findViewById(R.id.summarize_btn);
        saveSummaryBtn = findViewById(R.id.save_summary_btn);

        //User numeric input with min and max value
        numeric_input = findViewById(R.id.number_picker);
        numeric_input.setMinValue(0);
        numeric_input.setMaxValue(25);
        numeric_input.setValue(3);


        //On click listeners for buttons
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


        //Checking if the acticty was passed an intent from camera activity.
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
            //Convert Image to text with Firebase
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

    //Open camera activity and pass text from previous scans
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

    //Clears summary textview
    private void clearSumText(View view) {
        if (sTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Your text has already been cleared", Toast.LENGTH_LONG).show();
        } else {
            sTextView.setText("");
        }
    }

    //Clears Scanned textview
    private void clearScanText(View view) {
        if (textView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Your text has already been cleared", Toast.LENGTH_LONG).show();
        } else {
            textView.setText("");
        }
    }


    private void Summarize(View view) {
        Log.d(TAG, "Summarize method is running");
        //Checking if text is empty
        if (textView.getText().toString().isEmpty()) {
            Toast.makeText(this, "There seems to be no text to summarize", Toast.LENGTH_LONG).show();
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();


        //Using retrofit to connect to the Api
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .client(client)
                .build();
        placeholder = retrofit.create(Placeholder.class);

        Requests post = new Requests(numeric_input.getValue(), textView.getText().toString());
        Log.d(TAG, "This is what is the conents of the Requests object" + post.toString());


        //Creating a call object. What is sent to server
        Call<Requests> call = placeholder.createPost(post);
        Log.d(TAG, "call" + call);


        //Call<Requests> call = placeholder.createtest();
        Log.d(TAG,""+System.currentTimeMillis());
        //Post has been sent These are the results
        call.enqueue(new Callback<Requests>() {
            @Override
            public void onResponse(Call<Requests> call, Response<Requests> response) {
                Log.d(TAG,""+System.currentTimeMillis());
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Summarization has failed, Check your connection" + response);
                    return;
                }
                Log.d(TAG, "Response is successful");
                //Body of the response;
                Requests postResponse = response.body();
                String summaryContent = "";
                summaryContent += postResponse.getSummarised_text();
                sTextView.setText(summaryContent);
            }

            @Override
            public void onFailure(Call<Requests> call, Throwable t) {
                Log.d(TAG, "Failed to retrieve data\t" + t);
            }
        });
    }


    //Brings user to main activity
    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    //Saves text to Firebasefirestore and send user to the file view activity
    public void save_summary(View view) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (sTextView.getText().toString().length() < 5) {
            Toast.makeText(this, "You do not have enough text to save your Summary", Toast.LENGTH_LONG).show();
        } else {
            try {
                //Create a temporary file of the summarised text
                String data = sTextView.getText().toString();
                String filename = "";
                if (data.length() < 4) {
                    filename = data + "Summary";
                } else {
                    filename = data.substring(0, 9);
                }
                //Making an object of the summary
                Summary summary = new Summary(
                        filename,
                        data
                );
                db.collection(userId).document().set(summary)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(getApplicationContext(), ViewFilesActivity.class);
                                startActivity(intent);
                                finish();
                            }
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


