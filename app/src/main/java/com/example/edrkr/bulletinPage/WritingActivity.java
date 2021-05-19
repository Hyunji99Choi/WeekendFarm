package com.example.edrkr.bulletinPage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.edrkr.a_Network.Builder;
import com.example.edrkr.a_Network.Class.Post;
import com.example.edrkr.a_Network.Class.bulletin.GetBoard;
import com.example.edrkr.a_Network.Class.bulletin.GetEachBoard;
import com.example.edrkr.a_Network.Class.bulletin.PatchBoard;
import com.example.edrkr.a_Network.Class.bulletin.PostBoard;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.example.edrkr.mainpage.ControlMonitoring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

//확인 필요
public class WritingActivity extends AppCompatActivity {
    final static int TAKE_PICTURE = 1;
    final static int GET_IMAGE = 2;
    private int PERMISSION_REQUEST_CAMERA = 0;

    private EditText title;
    private EditText body;
    private Toolbar toolbar;
    private ImageView image;
    private ConstraintLayout f_image;
    private ImageButton img_delete;
    private int type;
    private Bitmap bitmapimage;
    private String TAG = "areum/Writingactivity"; //태그
    private int pos;
    private String URL = "forum/";
    private Board b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletinpage_writing);
        this.InitializeView(); //필요 요소 선언해주는 함수
    }


    public void InitializeView() { //id 연결 함수
        Log.v(TAG,"initialize");
        Intent intent = getIntent();
        b = (Board) intent.getSerializableExtra("board");
        type = intent.getIntExtra("type",0);
        title = (EditText) findViewById((R.id.title));
        body = (EditText) findViewById(R.id.body);
        image = (ImageView)findViewById(R.id.imageviewImage);
        f_image = (ConstraintLayout) findViewById(R.id.constraint_image);
        img_delete = (ImageButton) findViewById(R.id.image_delete_button);
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapimage = null;
                f_image.setVisibility(View.GONE);
            }
        });

        toolbar = findViewById(R.id.toolbar_writing);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, ControlMonitoring.GetInstance().getToolbarColor()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기존 타이틀 지우기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_goout); //뒤로가기 버튼 이미지

        if(type == 1){
            pos = intent.getIntExtra("pos",-1);
            getBoardData();
        }
    }

    public void getBoardData(){ //서버에서 게시글 데이터 가져오는 함수
        final ArrayList<Comment> dataset = new ArrayList<>();

        Log.v(TAG,"getBoardData 진입완료");

        //레트로핏 통신 기다리게 바꾸기
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getComment(URL+pos).enqueue(new Callback<GetEachBoard>() {
            @Override
            public void onResponse(Call<GetEachBoard> call, Response<GetEachBoard> response) { //통신 성공시
                if(response.isSuccessful()){
                    Log.v(TAG,response.body().toString());
                    GetEachBoard datas = response.body();
                    List<GetBoard> board = datas.getPost(); //게시글 부분
                    if(board.size()!=0) { //게시글 부분 가져오는 코드
                        Log.v(TAG,"board가 null이 아님");
                        title.setText(board.get(0).getTitle());
                        body.setText( board.get(0).getBody());
                        Log.v(TAG,"title : "+board.get(0).getTitle()+" body : "+ board.get(0).getBody());
                    }
                    else{
                        Log.v(TAG,"board size 0");
                    }
                }else{ //통신은 성공 but, 내부에서 실패
                    Log.v(TAG, "onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<GetEachBoard> call, Throwable t) { //통신 아예 실패
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void posttoserver(Board b) { //retrofit2를 사용하여 서버로 보내는 코드
//        Log.v(TAG, "posttoserver 진입완료");
//        PostBoard post = new PostBoard();
//        post.setNickname(b.getName());
//        post.setTitle(b.getTitle());
//        post.setContent(b.getBody());
//        post.setUserIdent(UserIdent.GetInstance().getUserIdent());
//        Log.v(TAG, "put 완료");
//
//        Call<PostBoard> call = retrofitIdent.GetInstance().getService().postData("forum/", post);
//        Builder builder = new Builder();
//        try {
//            builder.tryPost(call);

//                setResult(1);
//                finish();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.v(TAG, "tryconnect 완료");
        if(bitmapimage == null){
            Log.v(TAG,"이미지 없음");
        }else{
            testimage();
            Log.v(TAG, "testimage 완료");
        }
    }

    private void testimage(){
        Log.v(TAG, "test 시작");
        File f = savebitmap(bitmapimage);
        RequestBody filebody = RequestBody.create(MediaType.parse("image/*"), f);
        Log.v(TAG, "filebody 생성");
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("image", f.getName(), filebody);
        Log.v(TAG, "multipartBody 생성");

        Call<String> call = retrofitIdent.GetInstance().getService().request(b.getName(),UserIdent.GetInstance().getUserIdent(),b.getTitle(),b.getBody(),multipartBody);
        Log.v(TAG, "call 생성");
        call.enqueue(new Callback<String>() { //비동기 작업
            @Override
            public void onResponse(@EverythingIsNonNull Call<String> call, @EverythingIsNonNull  Response<String> response) { //성공 - 메인 스레드에서 처리
                if (response.isSuccessful()) {
                    //정상적으로 통신이 성공한 경우
                    Log.v(TAG, "onResponse: 성공, 결과\n" + response.body().toString());
                    setResult(1);
                    finish();
                } else {
                    //통신이 실패한 경우(응답코드 3xx,4xx 등)
                    Log.d(TAG,  "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(@EverythingIsNonNull Call<String> call,@EverythingIsNonNull  Throwable t) { //실패 - 메인 스레드에서 처리
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d(TAG, "onFailure: " +  t.getMessage());
            }
        });
    }

    private File savebitmap(Bitmap bmp){
        try{
            Log.v(TAG, "savebitmap 생성");
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File file = new File(directory,"temp.png");
            Log.v(TAG, "file 생성");
            if (file.exists()) { //기존에 파일이 존재한다면
                file.delete();
                Log.v(TAG, "file delete");
                file = new File(directory,"temp.png");
            }
            try {
                Log.v(TAG, "file 생성");
                FileOutputStream fos = null;
                fos = new FileOutputStream(file);
                bitmapimage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Log.v(TAG, "bmp.compres");
                fos.flush();
                Log.v(TAG, "outStream.flush();");
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return file;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void localsend(Board b){ //로컬로 데이터셋 지정
        //값을 notice로 넘기기 위한 작업
        Intent intent = new Intent();
        intent.putExtra("Board", b);
        intent.putExtra("chat_count", 0);
        Log.v(TAG, "intent에 저장 완료");

        //페이지 변경
        setResult(2, intent);
        Log.v(TAG, "intent 전송 완료");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home: { //뒤로가기 버튼 클릭시
                finish();
                return true;
            }
            case R.id.writing_next_button:{ //오른쪽 상단 확인버튼 클릭시
                Log.v(TAG, "전송 버튼 눌림");
                if(TextUtils.isEmpty(title.getText().toString()) ||TextUtils.isEmpty(body.getText().toString())){
                    Log.v(TAG,"내용이 빔");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((this));
                    alertDialogBuilder.setMessage("제목과 내용을 입력해주세요");
                    alertDialogBuilder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
                //현재 값을 저장
                b.setName(UserIdent.GetInstance().getNkname());
                b.setTitle(title.getText().toString());
                b.setBody(body.getText().toString());
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String date_ = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(date);
                b.setDate(date_);
                Log.v(TAG, "현재 값 저장완료");
                if(type == 0){
                    posttoserver(b);
                }else if(type == 1){
                    patchtoserver(b);
                }
//                 localsend(b);
                return true;
            }
            case R.id.writing_image_button:
                Log.v(TAG,"image select");
                getImage();
                break;
            case R.id.writing_camera_button :
                Log.v(TAG,"camera select");
                showCameraPreview();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GET_IMAGE);
    }

    //카메라 허락받기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == PERMISSION_REQUEST_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                startCamera();
            }
        }
    }

    private void showCameraPreview() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"권한 획득",Toast.LENGTH_SHORT).show();
            startCamera();
        }else{
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            Toast.makeText(getApplicationContext(), "이 앱은 카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }else{
            Toast.makeText(getApplicationContext(), "권한 획득 실패",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startCamera(){
        Log.v(TAG,"startCamera()");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        Log.v(TAG,"onActivityResult");
        super.onActivityResult(requestCode,resultCode,intent);
        switch (requestCode){
            case TAKE_PICTURE: //카메라 찍기
                Log.v(TAG,"TAKE_PICTURE");
                if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
                    Bundle extras = intent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    image.setImageBitmap(imageBitmap);
                    f_image.setVisibility(View.VISIBLE);
                }
                break;
            case GET_IMAGE: //이미지 가져오기
                Log.v(TAG,"GET_IMAGE");
                if(resultCode == RESULT_OK){
                    try{
                        InputStream in = getContentResolver().openInputStream(intent.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        bitmapimage = img;
                        in.close();
                        image.setImageBitmap(img);
                        f_image.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void patchtoserver(Board b) {
        Log.v(TAG,"patchtoserver 진입완료");
        PatchBoard post = new PatchBoard();
        post.setTitle(b.getTitle());
        post.setContent(b.getBody());
        Log.v(TAG,"put 완료");

        if(pos != -1) {
            Call<PatchBoard> call = retrofitIdent.GetInstance().getService().patchBoard("forum/"+pos, post);
            Builder builder = new Builder();
            try {
                builder.tryPost(call);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v(TAG, "tryconnect 완료");
        }else{
            Toast.makeText(this,"잠시후에 다시 시도해주세요",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_writing,menu);
        return true;
    }
}