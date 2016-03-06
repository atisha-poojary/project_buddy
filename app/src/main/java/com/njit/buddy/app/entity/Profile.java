package com.njit.buddy.app.entity;

/**
 * @author toyknight 11/23/2015.
 */
public class Profile {

    private String username;

    private String description;

    private boolean description_open;

    private String birthday;

    private boolean birthday_open;

    private String gender;

    private boolean gender_open;

    private String sexuality;

    private boolean sexuality_open;

    private String race;

    private boolean race_open;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescriptionOpen(boolean open) {
        this.description_open = open;
    }

    public boolean isDescriptionOpen() {
        return description_open;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthdayOpen(boolean open) {
        this.birthday_open = open;
    }

    public boolean isBirthdayOpen() {
        return birthday_open;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGenderOpen(boolean open) {
        this.gender_open = open;
    }

    public boolean isGenderOpen() {
        return gender_open;
    }

    public void setSexuality(String sexuality) {
        this.sexuality = sexuality;
    }

    public String getSexuality() {
        return sexuality;
    }

    public void setSexualityOpen(boolean open) {
        this.sexuality_open = open;
    }

    public boolean isSexualityOpen() {
        return sexuality_open;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRace() {
        return race;
    }

    public void setRaceOpen(boolean open) {
        this.race_open = open;
    }

    public boolean isRaceOpen() {
        return race_open;
    }

}
