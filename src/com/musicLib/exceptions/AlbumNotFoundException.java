package com.musicLib.exceptions;

public class AlbumNotFoundException extends NotFoundException {

    public AlbumNotFoundException() {
    }

    public AlbumNotFoundException(String message) {
        super(message);
    }

    public AlbumNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumNotFoundException(Throwable cause) {
        super(cause);
    }
}
