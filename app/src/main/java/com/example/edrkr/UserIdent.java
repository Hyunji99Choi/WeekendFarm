package com.example.edrkr;

public class UserIdent {
    private static final UserIdent Instance=new UserIdent(); //싱글턴 문법
    public static UserIdent GetInstance(){
        return Instance;
    }

    private String id;
    private String pw;
    private String name;
    private String nkname;
    private String farmCount;
    private String[] farmID;

    //setting
    public void setId(String id) { this.id = id; }
    public void setPw(String pw) { this.pw = pw; }
    public void setName(String name) { this.name = name; }
    public void setNkname(String nkname) { this.nkname = nkname; }
    public void setFarmCount(String farmCount) { this.farmCount = farmCount; }
    public void setFarmID(String[] farmID) { this.farmID = farmID; }

    //getting
    public String getId() { return id; }
    public String getPw() { return pw; }
    public String getName() { return name; }
    public String getNkname() { return nkname; }
    public String getFarmCount() { return farmCount; }
    public String[] getFarmID() { return farmID; }



    //생성자 제한
    private UserIdent(){}
}
