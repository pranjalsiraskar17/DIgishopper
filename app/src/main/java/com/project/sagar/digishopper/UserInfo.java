package com.project.sagar.digishopper;

public class UserInfo {
    String user_email;
    String user_pass;
    String user_fname;
    String user_lname;
    String user_phone_number;

    public UserInfo(String user_email, String user_pass, String user_fname, String user_lname, String user_phone_number) {
        this.user_email = user_email;
        this.user_pass = user_pass;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_phone_number = user_phone_number;
    }
    public UserInfo(){}

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }
}
