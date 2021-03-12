package com.example.edrkr.managerPage;

public class Member {
    private String id_;
    private String name_;
    private boolean checked_;

    Member(String id, String name){
        id_ = id;
        name_ = name;
        checked_ = false;
    }

    public String getId_() {
        return id_;
    }
    public String getName_() {
        return name_;
    }
    public boolean getChecked_(){return checked_;}
    public void setId_(String id_) {
        this.id_ = id_;
    }
    public void setName_(String name_) {
        this.name_ = name_;
    }
    public void setChecked_(boolean check){checked_ = check;}
}
