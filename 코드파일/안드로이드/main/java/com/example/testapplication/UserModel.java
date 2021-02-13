package com.example.testapplication;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("keyword1")
    private String keyword1;
    @SerializedName("keyword2")
    private String keyword2;
    @SerializedName("keyword3")
    private String keyword3;


    public UserModel(){

    }

    public UserModel(String user_id, String keyword1, String keyword2, String keyword3){
        this.user_id = user_id;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;

    }
    public void setUserId(String userId){ this.user_id = user_id; }
    public String getUserId() { return this.user_id; }

    public void setKeyword1(String keyword11){this.keyword1 = keyword11;}
    public String getKeyword1(){return this.keyword1;}

    public void setKeyword2(String keyword12){this.keyword2 = keyword12;}
    public String getKeyword2(){return this.keyword2;}

    public void setKeyword3(String keyword13){this.keyword3 = keyword13;}
    public String getKeyword3(){return this.keyword3;}

}
