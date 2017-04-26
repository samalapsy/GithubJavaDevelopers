package com.samalapsy.githubjavadevelopers;


import java.io.Serializable;

public class UserModel implements Serializable {
    String username, url, image;

    private static final long serialVersionUID = 1L;

    public UserModel(String username, String image, String url) {
        super();
        this.username = username;
        this.image = image;
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
