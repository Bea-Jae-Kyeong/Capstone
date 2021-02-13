package com.example.testapplication;

import com.google.gson.annotations.SerializedName;

public class ArticleModel2 {

    @SerializedName("news_id")
    private int news_id;
    @SerializedName("title")
    private String title;
    @SerializedName("source")
    private String source;
    @SerializedName("url")
    private String url;
    @SerializedName("img_url")
    private String img_url;
    @SerializedName("date")
    private String date;
    @SerializedName("contents")
    private String contents;
    @SerializedName("click_no")
    private int click_no;



    public ArticleModel2(){
        this.news_id = 0;
    }

    public ArticleModel2(int news_id, String title, String subtitle, String img_url, String date,String keyword1,String keyword2,String keyword3,String contents){
        this.news_id = news_id;
        this.title = title;
        this.source = subtitle;
        this.img_url = img_url;
        this.date = date;
        this.contents = contents;
    }
    public void setId(int news_id){ this.news_id = news_id; }
    public int getId() { return this.news_id; }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }

    public void setImgSrc(String img_url) {
        this.img_url = img_url;
    }
    public String getImgSrc(){
        return this.img_url;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getContents(){
        return this.contents;
    }

    public void setClickNo(int click_no) {
        this.click_no = click_no;
    }
    public int getClickNO(){
        return this.click_no;
    }

}
