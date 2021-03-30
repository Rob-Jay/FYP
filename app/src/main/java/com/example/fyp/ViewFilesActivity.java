package com.example.fyp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewFilesActivity extends AppCompatActivity {
    private static final String TAG = "ViewFilesActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Documents");
    private TextView textViewData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "I am Running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots) {
                    Summary summary = docSnapshot.toObject(Summary.class);
                    String title = summary.getTitle();
                    String description = summary.getTitle();
                    data += "Title: " + title + "\nDescription: " + description + "\n\n";
                    // Add to arraylist
                }
                textViewData.setText(data);
            }
        });
    }


    public void loadNotes(View view) {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots) {
                            Summary summary = docSnapshot.toObject(Summary.class);
                            String title = summary.getTitle();
                            String description = summary.getTitle();
                            data += "Title: " + title + "\nDescription: " + description + "\n\n";
                            // Add to arraylist
                        }
                        textViewData.setText(data);


                    }
                });

    }

    public void addNote(View view) {

    }
}