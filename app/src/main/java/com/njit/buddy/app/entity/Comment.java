package com.njit.buddy.app.entity;

/**
 * @author toyknight 3/5/2016.
 */
public class Comment {

    private final int uid;

    private final String username;

    private final long timestamp;

    private final String content;

    public Comment(int uid, String username, long timestamp, String content) {
        this.uid = uid;
        this.username = username;
        this.timestamp = timestamp;
        this.content = content;
    }

    public int getUID() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

}
