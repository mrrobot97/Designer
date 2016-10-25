package me.mrrobot97.designer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mrrobot on 16/10/21.
 */

public class Comment implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("body")
    private String body;
    @SerializedName("likes_count")
    private String likes_count;
    @SerializedName("likes_url")
    private String likes_url;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("user")
    private User user;

    public Comment(String id, String body, String likes_count, String likes_url, String created_at, User user) {
        this.id = id;
        this.body = body;
        this.likes_count = likes_count;
        this.likes_url = likes_url;
        this.created_at = created_at;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
