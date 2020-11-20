package com.example.edrkr;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserIdent {
    private static final UserIdent Instance=new UserIdent(); //싱글턴 문법
    public static UserIdent GetInstance(){
        return Instance;
    }



    private int nowMontriongFarm; //현재 내가 모니터링할 밭 배열 번호
    public int getNowMontriongFarm() { return nowMontriongFarm; }
    public void setNowMontriongFarm(int nowMontriongFarm) { this.nowMontriongFarm = nowMontriongFarm; }


    private String id;
    private String pw;
    private String name; //값 안받아옴.


    //관리자 권한 여부

    private String nkname; //닉네임
    private String phon; //폰 번호
    private String email; //이메일

    private int admin =0; //관리자 유무, 1이면 관리자

    private int farmCount; //가지고 있는 밭 개수
    private int[] farmID; //밭 id
    private String[] farmName; //밭 별명


    //json 객체 받기
    public void setJSONUserIdent(JSONObject JsonUser) throws JSONException {
        nkname = JsonUser.getString("UserName"); //닉네임
        phon = JsonUser.getString("UserPhoneNum"); //폰 번호
        email = JsonUser.getString("UserEmail"); //이메일

        //권한 여부, -1일 경우 관리자
        farmCount =  Integer.parseInt(JsonUser.getString("farmNum")) ; //밭 개수

        //관리 하는 밭 id
        JSONArray farm = JsonUser.getJSONArray("farmID");

        if(farmCount==-1){ //관리자 일 경우
            admin=1;
            farmCount=farm.length();
        }


        farmID = new int[farm.length()];
        for(int i = 0; i < farm.length(); i++){
            farmID[i]= (int)farm.get(i);
        }

        //관리 하는 밭 별명
        farm = JsonUser.getJSONArray("farmName");
        farmName = new String[farm.length()];
        for(int i = 0; i < farm.length(); i++){
            farmName[i]= (String) farm.get(i);
        }

        //배열 0번째 밭 초기 설정(현재 내가 모니터링 할 밭)
        nowMontriongFarm=0;
    }

    //디버그
    public void printLog(){
        //Log.w("id :",id);
        //Log.w("pw :",pw);
        Log.w("닉네임 :",nkname);
        Log.w("폰 :",phon);
        Log.w("email :",email);

        //권한여부

        Log.w("밭 개수 :",""+farmCount);
        //밭 id, 별명
        Log.w("밭 id :",""+farmID[0]);
        Log.w("밭 별명 :",""+farmName[0]);

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



    //생성자 제한
    private UserIdent(){}
}
