package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//View files activity allows users to view files from the firebase realtime database.
public class ViewFilesActivity extends AppCompatActivity {
    private static final String TAG = "ViewFilesActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference notebookRef = db.collection(userId);
    private SummaryAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchView;
    private ImageButton searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate is Running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);

        //Finding all entities from xml
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        searchBtn = findViewById(R.id.search_btn);

        //Recycler view holds all the content from the database
        setUpRecyclerView();

        //Setting onClickListener to search text button
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchView.getText().toString();
                Toast.makeText(ViewFilesActivity.this, "Clicked",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
/*Setting up recycler view. This method takes data from firebase and places it into the recycler view. The
recycler adapter uses SummaryAdapter Class to format the recycler view and view the documents
*/
    private void setUpRecyclerView() {
        Log.d(TAG, "Set Up Recycler View is running");
        Query query = notebookRef;
        FirestoreRecyclerOptions<Summary> options = new FirestoreRecyclerOptions.Builder<Summary>()
                .setQuery(query, Summary.class)
                .build();
        adapter = new SummaryAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "Set Up Recycler View has stopped running");


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteSummary(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    //Adapter start listening
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //Adapter stops listening
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //Brings user back to the main activity
    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}