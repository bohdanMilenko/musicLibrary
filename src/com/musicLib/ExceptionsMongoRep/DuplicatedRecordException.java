package com.musicLib.ExceptionsMongoRep;

public class DuplicatedRecordException extends Exception{

    public DuplicatedRecordException(String message) {
        super(message);
    }
}
