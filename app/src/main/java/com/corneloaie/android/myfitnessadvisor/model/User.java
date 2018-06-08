package com.corneloaie.android.myfitnessadvisor.model;

import android.media.Image;

import java.util.Date;

/**
 * Created by corneloaie on 29/10/2017.
 */


public class User {

    private int age;

    private Image avatar;

    private Date dateOfBirth;

    private String displayName;

    public User(int age, Image avatar, Date dateOfBirth, String displayName) {
        this.age = age;
        this.avatar = avatar;
        this.dateOfBirth = dateOfBirth;
        this.displayName = displayName;
    }

    public User() {
    }

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

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", avatar=" + avatar +
                ", dateOfBirth=" + dateOfBirth +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
