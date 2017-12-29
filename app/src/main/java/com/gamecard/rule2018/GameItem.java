package com.gamecard.rule2018;

/**
 * Created by sanglx on 12/29/17.
 */

public class GameItem {
    private String gameName;
    private String url;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GameItem() {
    }

    public GameItem(String gameName, String url) {
        this.gameName = gameName;
        this.url = url;
    }
}
