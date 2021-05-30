package com.deniszbecskei.personaldata.person;

import java.util.Date;

public class Attachment {
    private String contentType;
    private String language;
    private String url;
    private PositiveInt size;
    private String title;
    private Date creation;

    public Attachment() { }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PositiveInt getSize() {
        return size;
    }

    public void setSize(PositiveInt size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }
}
