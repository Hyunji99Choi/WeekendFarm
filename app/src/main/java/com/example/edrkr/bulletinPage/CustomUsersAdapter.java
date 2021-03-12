package com.example.edrkr.bulletinPage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;

import java.util.ArrayList;
//CommentAdapter와 설명 동일
public class CustomUsersAdapter extends RecyclerView.Adapter<CustomUsersAdapter.MyViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private OnItemClickListener mListener = null;
    private ArrayList<Board> mDataset = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        private TextView name;
        private TextView date;
        private TextView chat_count;

        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.board_title);
            name = v.findViewById(R.id.board_name);
            date = v.findViewById(R.id.board_date);
            chat_count = v.findViewById(R.id.board_count_chat);

            v.setOnClickListener(new View.OnClickListener() { //click 시
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION)   //position이 있다면
                    { //실제 click event 자리
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomUsersAdapter(ArrayList<Board> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomUsersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // create a new view
        View v = inflater.inflate(R.layout.boardnotice,parent,false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.v("알림","list에 적용");
        Board board = mDataset.get(position);
        Log.v("알림","position 가져옴 position : " + position);
        holder.title.setText(board.getTitle());
        Log.v("알림","title 가져옴 title :"+ board.getTitle());
        holder.name.setText(board.getName());
        Log.v("알림","Name 가져옴 Name :"+ board.getName());
        holder.chat_count.setText(""+board.getChat_count());
        Log.v("알림","Chat_count 가져옴 Chat_count :"+ board.getChat_count());
        holder.date.setText(board.getDate());
        Log.v("알림","Date 가져옴 Date :"+ board.getDate());
        Log.v("알림","list에 적용완료");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void changeDataset(ArrayList<Board> b){
        this.mDataset = b;
    }

}
