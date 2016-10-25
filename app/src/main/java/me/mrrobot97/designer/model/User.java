package me.mrrobot97.designer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mrrobot on 16/10/21.
 */

public class User implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("username")
    private String username;
    @SerializedName("html_url")
    private String html_url;
    @SerializedName("avatar_url")
    private String avatar_url;
    @SerializedName("buckets_count")
    private int buckets_count;
    @SerializedName("comments_received_count")
    private int comments_received_count;
    @SerializedName("followers_count")
    private int followers_count;
    @SerializedName("followings_count")
    private int followings_count;
    @SerializedName("projects_count")
    private int projects_count;
    @SerializedName("shots_count")
    private int shots_count;
    @SerializedName("type")
    private String type;
    @SerializedName("buckets_url")
    private String buckets_url;
    @SerializedName("followers_url")
    private String followers_url;
    @SerializedName("following_url")
    private String following_url;
    @SerializedName("projects_url")
    private String projects_url;
    @SerializedName("shots_url")
    private String shots_url;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("location")
    private String location;
    @SerializedName("links")
    private Links links;

    public User(String id, String name, String username, String html_url, String avatar_url, int buckets_count, int comments_received_count,
                int followers_count, int followings_count, int projects_count, int shots_count, String type, String buckets_url,
                String followers_url, String following_url, String projects_url, String shots_url, String created_at, String location,
                Links links) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.html_url = html_url;
        this.avatar_url = avatar_url;
        this.buckets_count = buckets_count;
        this.comments_received_count = comments_received_count;
        this.followers_count = followers_count;
        this.followings_count = followings_count;
        this.projects_count = projects_count;
        this.shots_count = shots_count;
        this.type = type;
        this.buckets_url = buckets_url;
        this.followers_url = followers_url;
        this.following_url = following_url;
        this.projects_url = projects_url;
        this.shots_url = shots_url;
        this.created_at = created_at;
        this.location = location;
        this.links = links;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getBuckets_count() {
        return buckets_count;
    }

    public void setBuckets_count(int buckets_count) {
        this.buckets_count = buckets_count;
    }

    public int getComments_received_count() {
        return comments_received_count;
    }

    public void setComments_received_count(int comments_received_count) {
        this.comments_received_count = comments_received_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int follwers_count) {
        this.followers_count = follwers_count;
    }

    public int getFollowings_count() {
        return followings_count;
    }

    public void setFollwings_count(int follwings_count) {
        this.followings_count = follwings_count;
    }

    public int getProjects_count() {
        return projects_count;
    }

    public void setProjects_count(int projects_count) {
        this.projects_count = projects_count;
    }

    public int getShots_count() {
        return shots_count;
    }

    public void setShots_count(int shots_count) {
        this.shots_count = shots_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuckets_url() {
        return buckets_url;
    }

    public void setBuckets_url(String buckets_url) {
        this.buckets_url = buckets_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public String getFollowing_url() {
        return following_url;
    }

    public void setFollowing_url(String following_url) {
        this.following_url = following_url;
    }

    public String getProjects_url() {
        return projects_url;
    }

    public void setProjects_url(String projects_url) {
        this.projects_url = projects_url;
    }

    public String getShots_url() {
        return shots_url;
    }

    public void setShots_url(String shots_url) {
        this.shots_url = shots_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
