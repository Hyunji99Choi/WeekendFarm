package com.example.edrkr.mainpage;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;

import java.util.ArrayList;

class DailymemoAdapter extends RecyclerView.Adapter<DailymemoAdapter.ViewHolder> {

    private ArrayList<Integer> mId;
    private ArrayList<String> mDay;
    private ArrayList<String> mData;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView contents;
        TextView day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 아이템 클릭 이벤트 처리.
            /*
            itemView.setLongClickable(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
            */
            // 뷰 객체에 대한 참조. (hold strong reference)
            contents = itemView.findViewById(R.id.textViewDailyText) ;
            day = itemView.findViewById(R.id.textViewDailyDate);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    DailymemoAdapter(ArrayList<Integer> id,ArrayList<String> d, ArrayList<String> ctx){
        mId = id;   mDay = d;   mData = ctx ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.view_dailymemo, parent, false);
        //DailymemoAdapter.ViewHolder vh = new DailymemoAdapter.ViewHolder(view);

        return new DailymemoAdapter.ViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.contents.setText(text);

        String d = mDay.get(position);
        holder.day.setText(d);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }


    //리사이클러뷰 페이지 전환 관련들
    public void updateData(ArrayList<Integer> id,ArrayList<String> d, ArrayList<String> ctx){
        mId.clear();    mDay.clear();   mData.clear();
        mId.addAll(id); mDay.addAll(d) ;    mData.addAll(ctx);
        notifyDataSetChanged();
    }
    public void clear(){
        int size = mId.size();
        mId.clear();    mDay.clear();   mData.clear();
        notifyItemRangeRemoved(0,size);
    }

}
