package com.samalapsy.githubjavadevelopers;


import java.io.Serializable;

public class UserModel implements Serializable {
    String username, profile_link, image;

    private static final long serialVersionUID = 1L;

    public UserModel(String username, String image, String profile_link) {
        super();
        this.username = username;
        this.image = image;
        this.profile_link = profile_link;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_link() {
        return profile_link;
    }

    public void setProfile_link(String profile_link) {
        this.profile_link = profile_link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
