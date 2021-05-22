package com.example.edrkr.h_network;

public class RequestUpdateUser {

    /*
            @Field("pw") String pw,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email
     */

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String pw;
    public String name;
    public String phone;
    public String email;
}
