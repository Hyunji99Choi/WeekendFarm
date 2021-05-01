package com.example.edrkr.managerPage;

public class Member { //사용자 클래스
    private int id_;
    private String userid_;
    private String name_;
    private boolean checked_;

    Member(int id, String userid, String name){
        this.id_ = id;
        userid_ = userid;
        name_ = name;
        checked_ = false;
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public String getUserid_() {
        return userid_;
    }
    public String getName_() {
        return name_;
    }
    public boolean getChecked_(){return checked_;}
    public void setUserid_(String userid_) {
        this.userid_ = userid_;
    }
    public void setName_(String name_) {
        this.name_ = name_;
    }
    public void setChecked_(boolean check){checked_ = check;}
}
