package me.mrrobot97.designer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mrrobot on 16/10/24.
 */

public class Links implements Serializable{
    @SerializedName("web")
    private String web;
    @SerializedName("twitter")
    private String twitter;

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public Links(String web, String twitter) {

        this.web = web;
        this.twitter = twitter;
    }
}
