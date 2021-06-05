package com.example.aasv11.database;

public class attendance {
    private String date;
    private String code;
    private String studId;
    private int att;
    private long count;

    public int getAtt() {
        return att;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setAtt(int att) {
        this.att = att;
    }

    public attendance() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }
}
