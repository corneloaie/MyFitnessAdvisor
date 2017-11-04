package com.corneloaie.android.myfitnessadvisor;

import android.media.Image;

import java.util.Date;

/**
 * Created by corneloaie on 29/10/2017.
 */

public class Profile {
    private int age;
    private Image avatar;
    private Date dateOfBirth;
    private String displayName;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
