package com.example.testapplication;

import com.google.gson.annotations.SerializedName;

public class KeywordModel {

    @SerializedName("keyword_no")
    private int keyword_no;
    @SerializedName("full_name")
    private String full_name;


    public KeywordModel(){
    }

    public KeywordModel(int keyword_no, String full_name){
        this.keyword_no = keyword_no;
        this.full_name = full_name;

    }
    public void setKeyword_no(int keyword_no){ this.keyword_no = keyword_no; }
    public int getKeyword_no() { return this.keyword_no; }

    public void setFull_name(String title) {
        this.full_name = full_name;
    }
    public String getFull_name(){
        return this.full_name;
    }


}
