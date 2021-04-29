package com.example.edrkr.a_Network.Class.manager;

import com.example.edrkr.a_Network.Class.Post;
import com.google.gson.annotations.SerializedName;

public class GetAllMember implements Post {
    @SerializedName("UserIdent")
    private int id;

    @SerializedName("UserId")
    private String userid;

    @SerializedName("UserName")
    private String username;

    GetAllMember(int id, String userid, String username){
        this.id = id;
        this.userid = userid;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "Id="+ id+
                ", userid="+ userid+
                ", username='" +username+'\''+
                '}';
    }
}
