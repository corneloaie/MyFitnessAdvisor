package com.corneloaie.android.myfitnessadvisor.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Profile.class,
        parentColumns = "idProfile",
        childColumns = "idProfile",
        onDelete = CASCADE))
public class Badge {

    @PrimaryKey(autoGenerate = true)
    private int idBadge;
    private String imageBadge;
    private String mobileDescription;
    private int timesAchieved;
    @TypeConverters({DateConverter.class})
    private Date dateTimeAchivied;
    private String name;
    private int idProfile;


    public int getIdBadge() {
        return idBadge;
    }

    public void setIdBadge(int idBadge) {
        this.idBadge = idBadge;
    }

    public String getImageBadge() {
        return imageBadge;
    }

    public void setImageBadge(String imageBadge) {
        this.imageBadge = imageBadge;
    }

    public String getMobileDescription() {
        return mobileDescription;
    }

    public void setMobileDescription(String mobileDescription) {
        this.mobileDescription = mobileDescription;
    }

    public int getTimesAchieved() {
        return timesAchieved;
    }

    public void setTimesAchieved(int timesAchieved) {
        this.timesAchieved = timesAchieved;
    }

    public Date getDateTimeAchivied() {
        return dateTimeAchivied;
    }

    public void setDateTimeAchivied(Date dateTimeAchivied) {
        this.dateTimeAchivied = dateTimeAchivied;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "imageBadge='" + imageBadge + '\'' +
                ", mobileDescription='" + mobileDescription + '\'' +
                ", timesAchieved=" + timesAchieved +
                ", dateTimeAchivied=" + dateTimeAchivied +
                ", name='" + name + '\'' +
                ", idProfile=" + idProfile +
                '}';
    }
}
