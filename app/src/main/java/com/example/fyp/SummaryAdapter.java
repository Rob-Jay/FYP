package com.example.fyp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


//This class extends FirestoreRecyclerAdapter to allow for the storage of the infromation in thedatabase inside a recycler view
public class SummaryAdapter extends FirestoreRecyclerAdapter<Summary, SummaryAdapter.SummaryHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SummaryAdapter(@NonNull FirestoreRecyclerOptions<Summary> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull SummaryHolder holder, int position, @NonNull Summary model) {
        Log.d("Test123", model.toString());
        holder.textViewDescription.setText(model.getSummary());
        holder.textViewTitle.setText(model.getTitle());
    }
    @NonNull
    @Override
    public SummaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item, parent, false);
        return new SummaryHolder(v);
    }

    public void deleteSummary(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class SummaryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;

        public SummaryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
        }
    }
}
