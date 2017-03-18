package com.luclx.nab.data.entities;

/**
 * Created by LucLX on 3/18/17.
 */

public class URL {
    private String url;
    private int type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public URL(String url, int type) {

        this.url = url;
        this.type = type;
    }
}
