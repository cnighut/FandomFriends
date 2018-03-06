package com.app.lenovo.fandomfriends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 02-03-2018.
 */

public class Profile {

    @SerializedName("Username")
    @Expose
    private String Username;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("Password")
    @Expose
    private String Password;

    @SerializedName("Level")
    @Expose
    private String Level;

    public String getUserName() {
        return Username ;
    }

    public void setUserName(String Username) {
        this.Username = Username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String Level) {
        this.Level = Level;
    }
}