package com.example.aasv11.database;

public class credentialsdb {
    private String User_Class;
    private String Email;
    private String Pass;
    private int ID;

    public String getUser_Class() {
        return User_Class;
    }

    public void setUser_Class(String user_Class) {
        User_Class = user_Class;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public credentialsdb() {
    }
}
