package com.example.oderingfood.models;

public class Image {
    private String id;
    private String url;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    public Image(String _id, String _url){
        this.id = _id;
        this.url =_url;
    }
}
