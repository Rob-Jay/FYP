package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class ViewFilesActivity extends AppCompatActivity {
    private static final String TAG = "ViewFilesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "I am Running");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("Documents");
        try {
            listRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            for (StorageReference prefix : listResult.getPrefixes()) {
                                Log.d(TAG, "This is Prefix");
                                Log.d(TAG, "" + prefix);


                                // All the prefixes under listRef.
                                // You may call listAll() recursively on them.
                            }

                            for (StorageReference item : listResult.getItems()) {
                                // All the items under listRef.
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Ha Fail");

                            // Uh-oh, an error occurred!
                        }
                    });
        }catch(Exception e){
            Log.d(TAG, "Error:\t" + e);

        }
        Log.d(TAG, "I skipped everything above");

    }
}