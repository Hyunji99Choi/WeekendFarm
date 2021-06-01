package com.example.edrkr;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.RetrofitClient;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class shingshing_info extends YouTubeBaseActivity {

        //상수 선언
        private static final int PERMISSION_CODE = 10;

        //통신으로 받아올 변수들
        //작물
        String firstName;//1등작물
        String secondName;//2등작물
        String thirdName;//3등작물

        // 1등 작물 설명
        String firstExp;//1등작물 설명 변수
        String firstImg;//1등작물 이미지

        // 병충해
        String BugName;//병충해 이름
        String[]BugExplain=new String[3];//병충해 설명
        String BugHardName;//병충해 피해 이름
        String BugImg;//병충해 이미지
        String BugHardImg;//병충해 피해이미지

        //작물정보 다운로드
        String downLink;//작물정보 다운로드 링크

        //유투브 변수
        String youTubePid;//나중에 받아올거임

        //youtube위한 변수들
        YouTubePlayerView youTubePlayerView;
        //String YoutubeKey= youtubekey.YoutubeKey;
        String YoutubeKey= "12342532445756785676435";
        YouTubePlayer.OnInitializedListener listener;
        YouTubePlayer player;
        //영상 스레드
        Thread thYou;

        //세팅에 필요한 변수들
        //-다운로드
        private long enqueue;
        private DownloadManager dm;
        //-UI
        ImageView cropImg;
        TextView stCropName;
        TextView ndCropName;
        TextView rdCropName;
        TextView cropName;
        TextView cropInfo;
        TextView insectTopic;
        TextView insectName;
        ImageView insectImg;
        TextView insectInfo;
        TextView insectDamegeInfo;
        TextView insectPreventInfo;
        TextView insectAImgName;
        ImageView insectAImg;
        TextView cookCropName;
        TextView downloadDate;
        Bitmap img1;
        Thread th;
        //-날짜
        Calendar cal;
        String date;
        //-로딩
        View progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shingshing_info);
        progressBar= (View) findViewById(R.id.llProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        getShingInfoData();//통신


        cropImg = (ImageView)findViewById(R.id.first_image);
        stCropName = (TextView)findViewById(R.id.stCropname);
        ndCropName = (TextView)findViewById(R.id.ndListCropName);
        rdCropName = (TextView)findViewById(R.id.rdListCropName);
        cropName = (TextView)findViewById(R.id.cropName);
        cropInfo = (TextView)findViewById(R.id.cropInfo);
        insectTopic = (TextView)findViewById(R.id.insTopic);
        insectName = (TextView)findViewById(R.id.insName);
        insectImg = (ImageView)findViewById(R.id.insImg);
        insectInfo = (TextView)findViewById(R.id.insInfo);
        insectDamegeInfo = (TextView)findViewById(R.id.insDamageInfo);
        insectPreventInfo = (TextView)findViewById(R.id.insPreventInfo);
        insectAImgName = (TextView)findViewById(R.id.insAIMGName);
        insectAImg = (ImageView)findViewById(R.id.insAIMG);
        cookCropName = (TextView)findViewById(R.id.cookName);
        downloadDate = (TextView)findViewById(R.id.downDate);
        youTubePlayerView=findViewById(R.id.youtubeView);

        cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        date = format.format(Calendar.getInstance().getTime());

    }

    public void YoutubeSet(String Pid){
        //youtube 재생용 스레드
        new Thread(new Runnable(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener=new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                youTubePlayer.loadVideo(Pid);
                            }
                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) { }
                        };
                        youTubePlayerView.initialize(YoutubeKey,listener);
                    }
                });
            }
        }).start();
    }
    //싱싱정보통 통신
    public void getShingInfoData(){
        Call<String> SingInfo= RetrofitClient.getApiService().
                getInfo();
        SingInfo.enqueue(new AutoRetryCallback<String>() {
            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                Log.e("싱싱정보통",t.getMessage());
            }

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("싱싱정보통 연결이 비정상적","error code:"+response.code());
                    return;
                }
                //Log.d("싱싱정보통 통신 성공적",response.body().toString());
                String value=response.body().replace("[\"","\"").replace("\"]","").replace("<br/>","\n\n").replace("\\r","\n").replace("\\n","\n");
                String[] splitvalue=value.split(",\"");


                for(int i=0;i<splitvalue.length;i++){
                    splitvalue[i]=splitvalue[i].replace("\"","");//통신 결과 받기;
                    Log.i("오는지 확인",splitvalue[i]);
                }
                progressBar.setVisibility(View.GONE);
                // 값 세팅
                firstName=splitvalue[0];//1등 이름
                secondName=splitvalue[1];//2등 이름
                thirdName=splitvalue[2];//3등 이름

                firstExp=splitvalue[3];//1등 설명
                firstImg=splitvalue[4];//1등 이미지

                BugName=splitvalue[5];//병해충 이름

                //이부분 아직 null이 온적이 없어서 null이 오면 어떻게 되는지 잘 모름
                if(splitvalue[6]==null)
                    BugExplain[0]="";//병충해 설명1 = 생태정보
                else
                    BugExplain[0]= splitvalue[6];//병충해 설명1

                if(splitvalue[7]==null)
                    BugExplain[1]="";//병충해 설명2 - 피해정보
                else
                    BugExplain[1]= splitvalue[7];//병충해 설명2

                if(splitvalue[8]==null)
                    BugExplain[2]="";//병충해 설명3 - 방제방법
                else
                    BugExplain[2]= splitvalue[8];//병충해 설명2


                BugHardName=splitvalue[9];//병충해 피해 이름
                BugHardImg=splitvalue[10];//병충해 피해이미지
                BugImg=splitvalue[11];//병충해 벌레 이미지

                downLink=splitvalue[12];//작물정보 다운로드 링크
                youTubePid=splitvalue[13];//유투브 키

                YoutubeSet(youTubePid);

                cropImg.setImageBitmap(getBitmap(firstImg));
                stCropName.setText(firstName);
                ndCropName.setText(secondName);
                rdCropName.setText(thirdName);
                cropName.setText(firstName);
                cropInfo.setText(firstExp);
                insectTopic.setText(firstName+"의 해충정보");
                insectName.setText(BugName);
                insectImg.setImageBitmap(getBitmap(BugImg));
                insectInfo.setText(BugExplain[0]);
                insectDamegeInfo.setText(BugExplain[1]);
                insectPreventInfo.setText(BugExplain[2]);
                insectAImgName.setText(BugHardName);
                insectAImg.setImageBitmap(getBitmap(BugHardImg));
                cookCropName.setText(firstName+" 요리 레시피");
                downloadDate.setText(" ~ "+date+" 기준 최신화");
            }
        });

    }
    //데이터 세팅
    private Bitmap getBitmap(String urlStr){
        th = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    img1 = BitmapFactory.decodeStream(is);
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        th.start();

        try{
            th.join();

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return img1;
    }

    //주간 농사정보 다운로드
    public void downloadClick(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permission = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission, PERMISSION_CODE);
        }else{
            downloading();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults){
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    downloading();
                }else{
                    Toast.makeText(this,"권한이 거부되었음", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void downloading(){
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downLink));

        request.setDescription("다운로드 중..");    // 다운로드 설명
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"주간농사정보.hwp");
        request.setNotificationVisibility(1);  // 상단바에 완료 결과 놔둠. 0 은 안뜸
        enqueue = dm.enqueue(request);
        Toast.makeText(this,"파일이 다운로드 됩니다..", Toast.LENGTH_SHORT).show();

    }

}
