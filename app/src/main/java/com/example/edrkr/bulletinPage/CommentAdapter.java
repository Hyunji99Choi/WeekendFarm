package com.example.edrkr.bulletinPage;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;

import java.util.ArrayList;
//각 게시판 게시글을 볼 때 사용하는 adapter
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<Comment> mDataset;
    private Context context;
    private onSwipeClickListener swipeClickListener = null;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    String TAG = "areum/commentAdapter";

    public CommentAdapter(Context context,ArrayList<Comment> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    //외부 클래스에 저장되어 있는 arraylist를 adapter에 복사
    public void changeDataset(ArrayList<Comment> myDataset) {
        this.mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        private TextView name; //작성자
        private TextView date; //작성일
        private TextView body; //댓글 본문
        private TextView buttonViewOption;

        public MyViewHolder(View v) { //각 댓글 모양을 잡아 놓은 곳에서 textview를 연결시킴
            super(v);
            name = v.findViewById(R.id.comment_name);
            date = v.findViewById(R.id.comment_date);
            body = v.findViewById(R.id.comment_textviewbody);
            buttonViewOption = itemView.findViewById(R.id.textViewOptions);
        }

        void bindData(Comment c){
            name.setText(c.getName());
            date.setText(c.getDate());
            body.setText(c.getBody());
            String nickname = UserIdent.GetInstance().getNkname();
            if(c.getName().compareTo(nickname)!=0){
                buttonViewOption.setVisibility(View.INVISIBLE);
                Log.v("arum.commentadapter","name : "+c.getName()+" body : "+c.getBody());
            }
        }
    }

    public interface onSwipeClickListener{
        void onDeleteCommentClick(View v,int pos);
        void onEditCommentClick(View v,int pos);
    }

    public void setSwipeClickListener(onSwipeClickListener listener){this.swipeClickListener = listener;}

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_bulletinpage_comment, parent,false);
        MyViewHolder vh = new MyViewHolder(v); //생성한 view를 담당할 viewholder 생성

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { //view 홀더 안에 있는 view에 댓글 내용 적용
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.closeLayout(String.valueOf(mDataset.get(position).getName()));
        holder.bindData(mDataset.get(position));

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //create a popup menu
                PopupMenu popup = new PopupMenu(context,holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.comment_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.edit_comment:
                                if(position!= RecyclerView.NO_POSITION)   //position이 있다면
                                { //실제 click event 자리
                                    if(swipeClickListener != null){
                                        swipeClickListener.onEditCommentClick(view,position);
                                    }
                                }
                                break;
                            case R.id.delete_comment:
                                if(position!= RecyclerView.NO_POSITION)   //position이 있다면
                                { //실제 click event 자리
                                    if(swipeClickListener != null){
                                        swipeClickListener.onDeleteCommentClick(view,position);
                                        Log.v(TAG,position+"번이 클릭됨");
                                    }
                                }
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        Log.v("알림","list에 적용완료");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    } //데이터 셋에 저장된 댓글 수 return

}

