package me.mrrobot97.designer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrrobot on 16/10/25.
 */

public class Attachment {
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("thumbnail_url")
    private String thumbnail_url;


    public Attachment(String id, String url, String thumbnail_url) {
        this.id = id;
        this.url = url;
        this.thumbnail_url = thumbnail_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}
