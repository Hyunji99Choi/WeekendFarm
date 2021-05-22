package com.example.edrkr.mainpage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.http.DELETE;

class DailymemoAdapter extends RecyclerView.Adapter<DailymemoAdapter.ViewHolder> {

    private ArrayList<Integer> mId;
    private ArrayList<String> mDay;
    private ArrayList<String> mData;

    private Context context;
    Dialog updateDialog;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView contents;
        TextView day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 아이템 클릭 이벤트 처리.

            itemView.setOnCreateContextMenuListener(this);

            // 뷰 객체에 대한 참조. (hold strong reference)
            contents = itemView.findViewById(R.id.textViewDailyText) ;
            day = itemView.findViewById(R.id.textViewDailyDate);
        }

        // 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록
        // ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001,1,"수정");
            MenuItem Delete = menu.add(Menu.NONE,1002,2,"삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }
        // 컨텐스트 메뉴에서 항목 클릭시 동작을 설정합니다.
        private  final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case 1001: // 수정 항목 선택시

                        //다이어그램
                        updateDialog = new Dialog(context);
                        updateDialog.setContentView(R.layout.today_writting_custom_dialog);
                        updateDialog.setCancelable(false); //취소 못함.
                        settingDialog(updateDialog);
                        FrameLayout ok = updateDialog.findViewById(R.id.check);
                        FrameLayout no = updateDialog.findViewById(R.id.back);
                        EditText editText = updateDialog.findViewById(R.id.body);
                        editText.setText(mData.get(getAdapterPosition()));
                        int editLength = editText.length();
                        Log.w("일지 내용 ", editText.toString());
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(editText.length()==0){
                                    Toast.makeText(context,"내용을 입력해주세요.",Toast.LENGTH_LONG).show();
                                }else{
                                    okDialog(mId.get(getAdapterPosition()),editText.getText().toString(),getAdapterPosition());
                                }
                            }
                        });
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(editLength == editText.length()){
                                    updateDialog.dismiss();
                                }else { //변경함.
                                    closeDialog();
                                }

                            }
                        });

                        updateDialog.show();
                        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

                        break;
                    case 1002: // 삭제 항목 선택시

                        //삭제 통신
                        ControlDailyMomo.GetInstance().deleteDaily(
                                mId.get(getAdapterPosition()),getAdapterPosition());

                        //mId.remove(getAdapterPosition());
                        break;
                }

                return true;
            }
        };
    }

    public void change(int position, String ctx){
        //내용 변경
        mData.set(position, ctx);
        notifyItemChanged(position);
    }

    public void delete(int position){
        mId.remove(position);
        mDay.remove(position);
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }


    // 생성자에서 데이터 리스트 객체를 전달받음.
    DailymemoAdapter(ArrayList<Integer> id,ArrayList<String> d, ArrayList<String> ctx, Context context){
        mId = id;   mDay = d;   mData = ctx ;   this.context = context;
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





    //다일로그
    private void settingDialog(final Dialog writDialog){
        TextInputLayout inputLayout = writDialog.findViewById(R.id.inputlayout);
        inputLayout.setCounterEnabled(true);
        inputLayout.setCounterMaxLength(140);
    }
    //일지 x버튼 눌렀을때 질문
    private void closeDialog(){
        //닫을지 묻기
        AlertDialog.Builder close = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
        close.setMessage("일지 수정을 취소하시겠습니까?")
                .setPositiveButton(Html.fromHtml("<font color='#D81B60'>예</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateDialog.dismiss(); //닫기
                    }
                }).setNegativeButton(Html.fromHtml("<font color='#D81B60'>아니요</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //취소 버튼
                Log.w("다일로그","아니요");
            }
        }).show();
    }
    //일지 v버튼 눌렀을때 질문
    private void okDialog(int id, String edittext,int position){
        // 저장 할지 묻기
        AlertDialog.Builder close = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
        close.setMessage("일지를 저장하시겠습니까?")
                .setPositiveButton(Html.fromHtml("<font color='#D81B60'>예</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateDialog.dismiss(); //닫기

                        //통신
                        ControlDailyMomo.GetInstance().updateDaily(id, position, edittext);
                        //!!!!!!!!!!!
                    }
                }).setNegativeButton(Html.fromHtml("<font color='#D81B60'>아니요</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //취소 버튼
                Log.w("다일로그","아니요");

            }
        }).show();
    }


}
