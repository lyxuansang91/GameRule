package com.gamecard.rule2018;

/**
 * Created by sanglx on 12/29/17.
 */

public class GameItem {
    private String gameName;
    private int gameZone;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameZone() {
        return gameZone;
    }

    public void setGameZone(int gameZone) {
        this.gameZone = gameZone;
    }

    public GameItem() {
    }

    public GameItem(String gameName, int gameZone) {
        this.gameName = gameName;
        this.gameZone = gameZone;
    }
}
