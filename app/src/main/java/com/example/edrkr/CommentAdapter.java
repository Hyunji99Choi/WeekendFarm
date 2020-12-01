package com.example.edrkr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<Comment> mDataset = null;

    public void changeDataset(ArrayList<Comment> myDataset) {
        this.mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name;
        private TextView date;
        private TextView body;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.comment_name);
            date = v.findViewById(R.id.comment_date);
            body = v.findViewById(R.id.comment_textviewbody);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(ArrayList<Comment> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // create a new view
        View v = inflater.inflate(R.layout.comment,parent,false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.v("알림","list에 적용");
        Comment comment = mDataset.get(position);
        Log.v("알림","position 가져옴 position : " + position);
        holder.name.setText(comment.getName());
        Log.v("알림","Name 가져옴 Name :"+ comment.getName());
        holder.date.setText(comment.getDate());
        Log.v("알림","Date 가져옴 Date :"+ comment.getDate());
        holder.body.setText(comment.getBody());
        Log.v("알림","Body 가져옴 Body :"+ comment.getBody());
        Log.v("알림","list에 적용완료");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

