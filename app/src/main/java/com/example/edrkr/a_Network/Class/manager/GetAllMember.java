package com.example.edrkr.a_Network.Class.manager;

import com.example.edrkr.a_Network.Class.Post;
import com.google.gson.annotations.SerializedName;

public class GetAllMember implements Post {
    @SerializedName("UserIdent")
    private int ident;

    @SerializedName("UserId")
    private String userid;

    @SerializedName("UserName")
    private String username;

    GetAllMember(int id, String userid, String username){
        this.ident = id;
        this.userid = userid;
        this.username = username;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int id) {
        this.ident = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //toString()을 Override 해주지 않으면 객체 주소값을 출력함.
    @Override
    public String toString(){
        return "PostResult{"+
                "Id="+ ident+
                ", userid="+ userid+
                ", username='" +username+'\''+
                '}';
    }
}
