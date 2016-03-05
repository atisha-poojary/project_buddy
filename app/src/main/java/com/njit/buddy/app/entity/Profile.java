package com.njit.buddy.app.entity;

/**
 * @author toyknight 11/23/2015.
 */
public class Profile {

    private String username;

    private String description;

    private String birthday;

    private String gender;

    private String sexuality;

    private String race;

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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setSexuality(String sexuality) {
        this.sexuality = sexuality;
    }

    public String getSexuality() {
        return sexuality;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRace() {
        return race;
    }

}
