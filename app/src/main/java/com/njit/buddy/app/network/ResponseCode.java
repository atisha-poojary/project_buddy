package com.njit.buddy.app.network;

/**
 * @author toyknight 11/8/2015.
 */
public class ResponseCode {

    public static final int SERVER_ERROR = -1;

    public static final int BAD_REQUEST = 0;

    public static final int BUDDY_OK = 1;

    public static final int EMAIL_NOT_AVAILABLE = 2;

    public static final int EMAIL_NOT_VALID = 3;

    public static final int PASSWORD_NOT_VALID = 4;

    public static final int PASSWORD_OR_EMAIL_MISS_MATCH = 5;

    public static final int USER_NOT_FOUND = 6;

    public static final int CONTENT_TOO_LONG = 7;

    public static final int VERIFICATION_CODE_ERROR = 8;

    public static final int VERIFICATION_CODE_EXPIRED = 9;

    public static final int LOGIN_REQUIRED = 100;

    public static final int MAIL_SENDING_TOO_FREQUENT = 101;

    public static final int REQUEST_NOT_SUPPORTED = 200;

    private ResponseCode() {
    }

    public static String toString(int error_code) {
        switch (error_code) {
            case BUDDY_OK:
                return "Buddy OK";
            case SERVER_ERROR:
                return "Server error";
            case BAD_REQUEST:
                return "Bad request";
            case LOGIN_REQUIRED:
                return "Login required";
            default:
                return "Not defined";
        }
    }

}
