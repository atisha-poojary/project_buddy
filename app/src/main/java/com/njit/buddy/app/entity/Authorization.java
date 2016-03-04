package com.njit.buddy.app.entity;

/**
 * @author toyknight 3/3/2016.
 */
public class Authorization {

    private final int uid;

    private final String authorization;

    public Authorization(int uid, String authorization) {
        this.uid = uid;
        this.authorization = authorization;
    }

    public int getUID() {
        return uid;
    }

    public String getAuthorization() {
        return authorization;
    }

}
