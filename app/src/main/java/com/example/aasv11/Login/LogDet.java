package com.example.aasv11.Login;


public class LogDet {
    private String Email;
    private String Pass;
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String user_Class) {
        this.Type = user_Class;
    }

    public LogDet() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        this.Pass = pass;
    }
}
