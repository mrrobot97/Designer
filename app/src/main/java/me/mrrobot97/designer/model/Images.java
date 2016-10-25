package me.mrrobot97.designer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mrrobot on 16/10/21.
 */

public class Images implements Serializable{
    @SerializedName("hidpi")
    private String hidpi;
    @SerializedName("normal")
    private String normal;
    @SerializedName("teaser")
    private String teaser;

    public Images(String hidpi, String normal, String teaser) {
        this.hidpi = hidpi;
        this.normal = normal;
        this.teaser = teaser;
    }

    public String getHidpi() {

        return hidpi;
    }

    public void setHidpi(String hidpi) {
        this.hidpi = hidpi;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }
}
