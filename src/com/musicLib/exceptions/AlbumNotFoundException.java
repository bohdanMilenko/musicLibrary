package com.musicLib.exceptions;

public class AlbumNotFoundException extends NotFoundException {

    public AlbumNotFoundException() {
    }

    public AlbumNotFoundException(String message) {
        super(message);
    }
}
