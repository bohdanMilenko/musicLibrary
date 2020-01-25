package com.musicLib.exceptions;

public class DuplicatedRecordException extends QueryException{


    public DuplicatedRecordException() {
    }

    public DuplicatedRecordException(String message) {
        super(message);
    }

    public DuplicatedRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedRecordException(Throwable cause) {
        super(cause);
    }
}
