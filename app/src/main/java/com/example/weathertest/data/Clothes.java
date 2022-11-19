package com.example.weathertest.data;

public class Clothes {
    private String clothesUrl;
    private String clothesComment;

    public Clothes() {
    }

    public Clothes(String clothesUrl, String clothesComment) {
        this.clothesUrl = clothesUrl;
        this.clothesComment = clothesComment;
    }

    public String getClothesUrl() {
        return clothesUrl;
    }

    public void setClothesUrl(String clothesUrl) {
        this.clothesUrl = clothesUrl;
    }

    public String getClothesComment() {
        return clothesComment;
    }

    public void setClothesComment(String clothesComment) {
        this.clothesComment = clothesComment;
    }
}