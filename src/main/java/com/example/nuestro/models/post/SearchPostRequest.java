package com.example.nuestro.models.post;

public class SearchPostRequest
{
    private String keyword;
    private  String userId;
    public SearchPostRequest(String keyword) {
        this.keyword = keyword;
    }

    public SearchPostRequest(String keyword, String userId) {
        this.keyword = keyword;
        this.userId= userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
