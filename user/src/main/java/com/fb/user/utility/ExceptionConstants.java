package com.fb.user.utility;

public enum ExceptionConstants {

    SERVER_ERROR("server.error"), AUTHENTICATION_FAILED("authentication.failed"),
    USER_ALREADY_PRESENT("user.already.present"),USERNAME_ALREADY_PRESENT("username.already.present"), USER_NOT_FOUND("user.not.found");

    private final String type;

    private ExceptionConstants(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
