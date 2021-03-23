package com.example.edrkr.bulletinPage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.edrkr.R;

import java.util.ArrayList;
//CommentAdapter와 설명 동일
public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.MyViewHolder> {

    private Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private OnItemClickListener mListener = null;
    private ArrayList<Board> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BulletinAdapter(Context context,ArrayList<Board> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    public void setBoard(ArrayList<Board> mDataset){
        this.mDataset = new ArrayList<>();
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // create a new view
        View view = LayoutInflater.from(context).inflate(R.layout.view_bulletinpage_board, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
//        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(mDataset.get(position).getName()));
        viewBinderHelper.closeLayout(String.valueOf(mDataset.get(position).getName()));
        holder.bindData(mDataset.get(position));

    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        private TextView name;
        private TextView date;
        private TextView chat_count;
        private SwipeRevealLayout swipeRevealLayout;

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

        void bindData(Board board){
            title.setText(board.getTitle());
            name.setText(board.getName());
            date.setText(board.getDate());
            chat_count.setText(Integer.toString(board.getChat_count()));
        }
    }

    public void changeDataset(ArrayList<Board> b){
        this.mDataset = b;
    }

}
