package com.njit.buddy.app.entity;

/**
 * @author toyknight 11/20/2015.
 */
public class Post {

    public static final int CONFESS = 0;
    public static final int ASK = 1;
    public static final int VENT = 2;
    public static final int LAUGH = 3;
    public static final int ENCOURAGE = 4;
    public static final int ANNOUNCE = 5;

    private final int pid;
    private final int uid;
    private final String username;
    private final String content;
    private final int category;
    private final long timestamp;

    private int hugs;
    private int comments;

    private boolean flagged;
    private boolean belled;
    private boolean hugged;

    public Post(int pid, int uid, String username, String content, int category, long timestamp) {
        this.pid = pid;
        this.uid = uid;
        this.username = username;
        this.content = content;
        this.category = category;
        this.timestamp = timestamp;
    }

    public int getPID() {
        return pid;
    }

    public int getUID() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public int getCategory() {
        return category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setHugs(int hugs) {
        this.hugs = hugs;
    }

    public int getHugs() {
        return hugs;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getComments() {
        return comments;
    }

    public void setFlagged(boolean b) {
        this.flagged = b;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setBelled(boolean b) {
        this.belled = b;
    }

    public boolean isBelled() {
        return belled;
    }

    public void setHugged(boolean b) {
        this.hugged = b;
    }

    public boolean isHugged() {
        return hugged;
    }

}
