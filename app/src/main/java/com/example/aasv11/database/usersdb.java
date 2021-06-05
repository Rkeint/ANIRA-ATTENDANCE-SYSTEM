package com.example.aasv11.database;

public class usersdb {
    private String FName;
    private String MName;
    private String LName;
    private String Gender;
    private String User_Class;
    private String Email;
    private String Pass;
    private String Date;
    private String class1;
    private String class2;
    private String class3;
    private String class4;
    private String class5;
    private int ID;

    public String getClass1() {
        return class1;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    public String getClass5() {
        return class5;
    }

    public void setClass5(String class5) {
        this.class5 = class5;
    }



    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public usersdb() {
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String fname) {
        this.FName = fname;
    }

    public String getMName() {
        return MName;
    }

    public void setMName(String mname) {
        this.MName = mname;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getUser_Class() {
        return User_Class;
    }

    public void setUser_Class(String user_Class) {
        this.User_Class = user_Class;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
