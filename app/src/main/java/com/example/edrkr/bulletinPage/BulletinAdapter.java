package com.example.edrkr.bulletinPage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.edrkr.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//CommentAdapter와 설명 동일
public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.MyViewHolder> {

    private Context context;
    String TAG = "areum/bulletinadapter";
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private OnItemClickListener mListener = null;
    private onSwipeClickListener swipeClickListener = null;
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
        // create a new view
        View view = LayoutInflater.from(context).inflate(R.layout.view_bulletinpage_board, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(mDataset.get(position).getName()));
        viewBinderHelper.closeLayout(String.valueOf(mDataset.get(position).getName()));
        holder.bindData(mDataset.get(position));

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public interface onSwipeClickListener{
        void onItemClick(View v, int pos);
        void onDeleteClick(View v,int pos);
        void onEditClick(View v,int pos);
    }

    public void setSwipeClickListener(onSwipeClickListener listener){this.swipeClickListener = listener;}

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        private TextView name;
        private TextView date;
        private TextView chat_count;
        //swipereeallayout용
        private LinearLayout mainlayout;
        private ImageView txtEdit;
        private ImageView txtDelete;
        private SwipeRevealLayout swipeRevealLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainlayout = itemView.findViewById(R.id.mainlayout);
            title = itemView.findViewById(R.id.board_title);
            name = itemView.findViewById(R.id.board_name);
            date = itemView.findViewById(R.id.board_date);
            chat_count = itemView.findViewById(R.id.board_count_chat);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            swipeRevealLayout = itemView.findViewById(R.id.swipelayout);

            mainlayout.setOnClickListener(new View.OnClickListener() { //click 시
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"mainlayout onclick");
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION)   //position이 있다면
                    { //실제 click event 자리
                        if(swipeClickListener != null){
                            swipeClickListener.onItemClick(v,pos);
                        }
                    }
                }
            });
            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"Edit is Clicked");
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION)   //position이 있다면
                    { //실제 click event 자리
                        if(swipeClickListener != null){
                            swipeClickListener.onEditClick(v,pos);
                        }
                    }
//                    Toast.makeText(context,"Edit is Clicked",Toast.LENGTH_LONG).show();
                }
            });

            txtDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.v(TAG,"Deleted is Clicked");
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION)   //position이 있다면
                    { //실제 click event 자리
                        if(swipeClickListener != null){
                            swipeClickListener.onDeleteClick(v,pos);
                            Log.v(TAG,pos+"번이 클릭됨");
                        }
                    }
//                    Toast.makeText(context,"Deleted is Clicked",Toast.LENGTH_LONG).show();
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
