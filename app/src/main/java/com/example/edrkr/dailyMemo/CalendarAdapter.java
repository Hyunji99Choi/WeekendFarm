package com.example.edrkr.dailyMemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {
    private String tag = "CalendarAdapter";
    private List<MyCalendar> mCalendar;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tb_day, tb_date;

        public MyViewHolder(View view){
            super(view);
            tb_day = (TextView)view.findViewById(R.id.day_1);
            tb_date = (TextView)view.findViewById(R.id.date_1);
        }
    }

    public CalendarAdapter (List<MyCalendar> mCalendar){
        this.mCalendar = mCalendar;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dailymemo_date, parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        MyCalendar calendar = mCalendar.get(position);

        holder.tb_day.setText(calendar.getDay());

        holder.tb_date.setText(calendar.getDate());
    }

    @Override
    public int getItemCount() {
        return mCalendar.size();
    }
}
