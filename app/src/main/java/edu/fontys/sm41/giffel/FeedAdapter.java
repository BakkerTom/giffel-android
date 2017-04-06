package edu.fontys.sm41.giffel;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by tom on 30/03/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<GifHolder> {
    private DatabaseReference ref;
    private ArrayList<Gif> items;

    public FeedAdapter(DatabaseReference ref) {
        this.ref = ref;
        items = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot postSnap : dataSnapshot.getChildren()){
                    Gif gif = postSnap.getValue(Gif.class);
                    items.add(gif);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database", "DatabaseError: " + databaseError);
            }
        });
    }

    @Override
    public GifHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gif_cell, parent, false);
        return new GifHolder(view);
    }

    @Override
    public void onBindViewHolder(GifHolder holder, int position) {
        holder.bindGif(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
