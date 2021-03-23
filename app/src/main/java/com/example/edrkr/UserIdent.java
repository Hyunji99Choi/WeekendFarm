package com.example.edrkr;

import android.util.Log;

import com.example.edrkr.h_network.ResponseUserIdent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserIdent {

    private static final UserIdent Instance=new UserIdent(); //싱글턴 문법
    public static UserIdent GetInstance(){
        return Instance;
    }

    /*
    private static UserIdent Instance=null;
    public static UserIdent GetInstance(Context context){
        if(Instance==null){
            Instance = new UserIdent();
        }
        return Instance;
    }//싱글턴 문법
    */
    private int nowMontriongFarm; //현재 내가 모니터링할 밭 배열 번호
    public int getNowMontriongFarm() { return nowMontriongFarm; }
    public void setNowMontriongFarm(int nowMontriongFarm) { this.nowMontriongFarm = nowMontriongFarm; }


    private String id;
    private String pw;
    private String name; //값 안받아옴.



    private int UserIdent; //유저 고유 번호 *****

    private String nkname; //닉네임
    private String phon; //폰 번호
    private String email; //이메일

    private int admin =0; //관리자 유무, 1이면 관리자

    private int farmCount; //가지고 있는 밭 개수
    private int[] farmID; //밭 id
    private String[] farmName; //밭 별명

    //ResponseUserIdent 객체 받기
    public void setResponseUserIdent(ResponseUserIdent userIdent) {
        nkname = userIdent.getUserNickName(); //닉네임
        phon = userIdent.getUserPhoneNum(); //폰 번호
        email = userIdent.getUserEmail(); //이메일
        name = userIdent.getUserName(); //이름
        UserIdent = Integer.parseInt(userIdent.getUserIdent()); //유저 고유번호



        //권한 여부, -1일 경우 관리자
        farmCount =  Integer.parseInt(userIdent.getFarmNum()) ; //밭 개수

        if(farmCount==-1){ //관리자 일 경우
            admin=1;
            farmCount=userIdent.getFarmID().size();
        }
        Log.w("싱글턴","의심 0");
        //JSONArray farm;

        //관리 하는 밭 id
        farmID = new int[userIdent.getFarmID().size()];
        for(int i = 0; i < userIdent.getFarmID().size(); i++){
            farmID[i]= userIdent.getFarmID().get(i).getAsInt();
        }
        Log.w("싱글턴","의심 1");

        //관리 하는 밭 별명
        //farm = userIdent.getFarmName();
        farmName = new String[userIdent.getFarmName().size()];
        for(int i = 0; i < userIdent.getFarmName().size(); i++){
            farmName[i]= userIdent.getFarmName().get(i).getAsString();
        }

        Log.w("싱글턴","의심 2");

        //배열 0번째 밭 초기 설정(현재 내가 모니터링 할 밭)
        nowMontriongFarm=0;

        Log.w("싱글턴","통과");
    }



    //디버그 무명 사용자 생성
    public void settingNothing(){
        id="111";
        pw="111";

        nkname ="이름없음"; //닉네임
        phon = "000-0000-0000"; //폰 번호
        email = "이름없음@gmail.com"; //이메일


        farmCount =  0; //밭 개수

    }


    //디버그
    public void printLog(){
        Log.w("id :",id);
        Log.w("pw :",pw);
        Log.w("닉네임 ",nkname);
        Log.w("폰 ",phon);
        Log.w("email ",email);

        //권한여부

        Log.w("밭 개수 ",""+farmCount);
        //밭 id, 별명
        Log.w("밭 id ",""+farmID[0]);
        Log.w("밭 별명 ",""+farmName[0]);

    }


    //setting
    public void setId(String id) { this.id = id; }
    public void setPw(String pw) { this.pw = pw; }
    public void setNkname(String nkname) { this.nkname = nkname; }
    public void setPhon(String phon) { this.phon = phon; }
    public void setEmail(String email) { this.email = email; }
    public void setFarmCount(int farmCount) { this.farmCount = farmCount; }
    public void setFarmID(int[] farmID) { this.farmID = farmID; }
    public void setFarmName(String[] farmName) { this.farmName = farmName; }
    public void setUserIdent(int userIdent) { UserIdent = userIdent; }

    //getting
    public String getId() { return id; }
    public String getPw() { return pw; }
    //public String getName() { return name; }
    public String getNkname() { return nkname; }
    public int getFarmCount() { return farmCount; }
    public int getFarmID(int i) { return farmID[i]; }
    public String getPhon() { return phon; }
    public String getEmail() { return email; }
    public int getAdmin() { return admin; }
    public String getFarmName(int i) { return farmName[i]; }
    public int getUserIdent() { return UserIdent; }



    //생성자 제한
    private UserIdent(){}
}
