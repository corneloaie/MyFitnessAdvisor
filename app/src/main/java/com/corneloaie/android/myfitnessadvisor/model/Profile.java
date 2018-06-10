package com.corneloaie.android.myfitnessadvisor.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;


@Entity
public class Profile {


    @PrimaryKey(autoGenerate = true)
    private int idProfile;
    private int age;
    private String avatar;
    @TypeConverters({DateConverter.class})
    private Date dateOfBirth;
    private String displayName;
    @TypeConverters({DateConverter.class})
    private Date memberSince;
    private int height;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
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

    public Date getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince = memberSince;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", avatar=" + avatar +
                ", dateOfBirth=" + dateOfBirth +
                ", displayName='" + displayName + '\'' +
                ", memberSince=" + memberSince +
                ", height=" + height +
                '}';
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }
}
