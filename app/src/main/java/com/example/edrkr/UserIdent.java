package com.example.edrkr;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIdent {
    private static final UserIdent Instance=new UserIdent(); //싱글턴 문법
    public static UserIdent GetInstance(){
        return Instance;
    }



    private String id;
    private String pw;
    private String name;


    //관리자 권한 여부

    private String nkname; //닉네임
    private String phon; //폰 번호
    private String email; //이메일

    private int admin; //관리자 유무

    private int farmCount; //가지고 있는 밭 개수
    private String[] farmID; //밭 id
    private String[] farmName; //밭 별명

    //json 객체 받기
    public void setJSONUserIdent(JSONObject JsonUser) throws JSONException {
        nkname = JsonUser.getString("UserName"); //닉네임
        phon = JsonUser.getString("UserPhoneNum"); //폰 번호
        email = JsonUser.getString("UserEmail"); //이메일

        //권한 여부

        farmCount = JsonUser.getInt("farmNum"); //밭 개수
        //farmID
        //farmname
    }

    //디버그
    public void printLog(){
        Log.w("id :",id);
        Log.w("pw :",pw);
        Log.w("닉네임 :",nkname);
        Log.w("폰 :",phon);
        Log.w("email :",email);

        //권한여부

        Log.w("밭 개수 :",""+farmCount);
        //밭 id, 별명


    }


    //setting
    public void setId(String id) { this.id = id; }
    public void setPw(String pw) { this.pw = pw; }
    public void setNkname(String nkname) { this.nkname = nkname; }
    public void setPhon(String phon) { this.phon = phon; }
    public void setEmail(String email) { this.email = email; }
    public void setFarmCount(int farmCount) { this.farmCount = farmCount; }
    public void setFarmID(String[] farmID) { this.farmID = farmID; }
    public void setFarmName(String[] farmName) { this.farmName = farmName; }

    //getting
    public String getId() { return id; }
    public String getPw() { return pw; }
    public String getName() { return name; }
    public String getNkname() { return nkname; }
    public int getFarmCount() { return farmCount; }
    public String[] getFarmID() { return farmID; }
    public String getPhon() { return phon; }
    public String getEmail() { return email; }
    public int getAdmin() { return admin; }
    public String[] getFarmName() { return farmName; }



    //생성자 제한
    private UserIdent(){}
}
