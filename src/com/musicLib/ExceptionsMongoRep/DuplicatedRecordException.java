package com.musicLib.ExceptionsMongoRep;

public class DuplicatedRecordException extends RuntimeException{

    public DuplicatedRecordException(String message) {
        super(message);
    }
}
