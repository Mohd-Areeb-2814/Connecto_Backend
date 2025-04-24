package com.fb.posts.utility;

public enum ExceptionConstants {

    SERVER_ERROR("server.error"), POST_NOT_FOUND("post.not.found");

    private final String type;

    private ExceptionConstants(String type) {

        this.type = type;

    }

    @Override
    public String toString() {
        return this.type;
    }

}