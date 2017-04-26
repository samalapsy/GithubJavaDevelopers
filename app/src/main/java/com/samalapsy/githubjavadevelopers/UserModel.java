package com.samalapsy.githubjavadevelopers;


import java.io.Serializable;

public class UserModel implements Serializable {
    String username, avatar, image;

    private static final long serialVersionUID = 1L;

    public UserModel(String username, String image, String avatar) {
        super();
        this.username = username;
        this.image = image;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
