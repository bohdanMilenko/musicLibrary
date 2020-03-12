package com.musicLib.exceptions;

public class DbConnectionException extends Exception {

    public DbConnectionException() {
    }

    public DbConnectionException(String message) {
        super(message);
    }

    public DbConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConnectionException(Throwable cause) {
        super(cause);
    }
}
