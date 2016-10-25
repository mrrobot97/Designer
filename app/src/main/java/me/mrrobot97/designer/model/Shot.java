package me.mrrobot97.designer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mrrobot on 16/10/21.
 */

public class Shot implements Serializable{
    public Shot(String id, String title, String description, int width, String height, Images images, int comments_count
            , int views_count, int likes_count, int attachments_count, String created_at, String html_url, String attachments_url
            , String comments_url, boolean animated, String[] tags, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.width = width;
        this.height = height;
        this.images = images;
        this.comments_count = comments_count;
        this.views_count = views_count;
        this.likes_count = likes_count;
        this.attachments_count = attachments_count;
        this.created_at = created_at;
        this.html_url = html_url;
        this.attachments_url = attachments_url;
        this.comments_url = comments_url;
        this.animated = animated;
        this.tags = tags;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getAttachments_count() {
        return attachments_count;
    }

    public void setAttachments_count(int attachments_count) {
        this.attachments_count = attachments_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAttachments_url() {
        return attachments_url;
    }

    public void setAttachments_url(String attachments_url) {
        this.attachments_url = attachments_url;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private String height;
    @SerializedName("images")
    private Images images;
    @SerializedName("comments_count")
    private int comments_count;
    @SerializedName("views_count")
    private int views_count;
    @SerializedName("likes_count")
    private int likes_count;
    @SerializedName("attachments_count")
    private int attachments_count;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("html_url")
    private String html_url;
    @SerializedName("attachments_url")
    private String attachments_url;
    @SerializedName("comments_url")
    private String comments_url;
    @SerializedName("animated")
    private boolean animated;
    @SerializedName("tags")
    private String[] tags;
    @SerializedName("user")
    private User user;
}
