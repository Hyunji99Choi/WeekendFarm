package com.example.edrkr.managerPage;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.bulletinPage.CustomUsersAdapter;
import com.example.edrkr.R;

import java.util.ArrayList;

public class stringadapter extends RecyclerView.Adapter<stringadapter.MyViewHolder> {
    private String URL = "http://52.79.237.95:3000/forum/test";
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }
    private CustomUsersAdapter.OnItemClickListener mListener = null;
    private ArrayList<Member> mDataset = null;
    int type; // 0 : member 1:area 2 :member_each_area_shape 3: memberhas

    public void setOnItemClickListener(CustomUsersAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView id;
        public TextView name;
        public Button button;

        public MyViewHolder(View v) {
            super(v);
            if(type == 0) { //member
                name = v.findViewById(R.id.Textview_memberName);
                id = v.findViewById(R.id.textView_memberID);
            }
            else if(type == 1){   //area
                name = v.findViewById(R.id.shape_area_name);
            }else if(type == 2){  //member_each_area_shape
                name = v.findViewById(R.id.member_each_area_shape_name);
                button = v.findViewById(R.id.shape_member_deleteButton);
            }else if(type == 3){  //memberhas
                name = v.findViewById(R.id.Textview_memberhasName);
                button = v.findViewById(R.id.Button_memberhasDelete);
                id = v.findViewById(R.id.Textview_memberhasNameID);
            }

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
    public stringadapter(ArrayList<Member> myDataset, int type) {
        this.type = type;
        mDataset = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public stringadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // create a new view
        View v;
        if(type == 0) { //member
            v = inflater.inflate(R.layout.shape_membername, parent, false);
        }
        else if(type == 1){//area
            v = inflater.inflate(R.layout.shape_areanumber, parent, false);
        }
        else if(type == 2) {//member_each_area_shape
            v = inflater.inflate(R.layout.member_each_area_shape, parent, false);
        }
        else if(type == 3){//memberhas
            v = inflater.inflate(R.layout.shape_memeberhas, parent, false);
        }
        else{
            v = null;
        }
        stringadapter.MyViewHolder vh = new stringadapter.MyViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final stringadapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset.get(position).getChecked_()){
            holder.itemView.setBackgroundColor(Color.GREEN);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        if(type >= 2){
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    DeleteItem(v,position);
                }
            });
        }
        Log.v("알림","list에 적용");
        Member str = mDataset.get(position);
        holder.name.setText(str.getName_());
        if(type == 0||type ==3){
            holder.id.setText(str.getId_());
        }
        Log.v("알림","Name 가져옴 Name :"+ str);
        Log.v("알림","list에 적용완료");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void DeleteItem(View v, int position){
        Log.v("stringadapter","pos : "+position+" delete클릭");
        //delete 코드만들기
//        NetworkTask getboardlist_networkTask = new NetworkTask(URL,null); //networktast 설정 부분
//        Log.v("stringadapter","networktask 입력 성곧");
//        try {
//            getboardlist_networkTask.execute().get(); //설정한 networktask 실행
//            Log.v("stringadapter","delete 실행 완료");
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            Log.v("stringadapter", "executionexception");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Log.v("stringadapter", "interruptedexception");
//        }
//        Log.v("stringadapter","execute 확인");
//        String result = getboardlist_networkTask.result;
//        Log.v("stringadapter","result 확인 result : "+result);

    }
}
