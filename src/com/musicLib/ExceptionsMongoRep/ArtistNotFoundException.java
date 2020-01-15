package com.musicLib.ExceptionsMongoRep;

public class ArtistNotFoundException extends Exception {

    public ArtistNotFoundException(String message) {
        super(message);
    }
}
