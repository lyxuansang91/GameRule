package com.readnews.app2018;

/**
 * Created by sanglx on 12/29/17.
 */

public class Item {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Item() {
    }

    public Item(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
