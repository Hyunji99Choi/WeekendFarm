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
//각 게시판 게시글을 볼 때 사용하는 adapter
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<Comment> mDataset;

    //외부 클래스에 저장되어 있는 arraylist를 adapter에 복사
    public void changeDataset(ArrayList<Comment> myDataset) {
        this.mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name; //작성자
        private TextView date; //작성일
        private TextView body; //댓글 본문

        public MyViewHolder(View v) { //각 댓글 모양을 잡아 놓은 곳에서 textview를 연결시킴
            super(v);
            name = v.findViewById(R.id.comment_name);
            date = v.findViewById(R.id.comment_date);
            body = v.findViewById(R.id.comment_textviewbody);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentAdapter(ArrayList<Comment> myDataset) {
        mDataset = myDataset;
    } //생성자

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext(); //부모의 필드(?)를 가져옴
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE); //inflater 지정
        // create a new view
        View v = inflater.inflate(R.layout.comment,parent,false); //댓글을 적용시킬 view를 생성
        MyViewHolder vh = new MyViewHolder(v); //생성한 view를 담당할 viewholder 생성

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) { //view 홀더 안에 있는 view에 댓글 내용 적용
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
    } //데이터 셋에 저장된 댓글 수 return
}

