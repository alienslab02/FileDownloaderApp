package com.bilal.filedownloaderapp.models;

/**
 * Created by applepc on 06/08/2017.
 */

public class User extends BaseModel {

    String username, name;
    Image profileImage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }
}
