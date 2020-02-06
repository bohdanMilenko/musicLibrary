package com.musicLib.repository.MongoDBRepisotory;

public class MetaDataMongo {

    static final String SONGS_COLLECTION = "songs";
    static final String ARTISTS_COLLECTION = "artists";

    static final String ID = "_id";
    static final String ARTIST_NAME = "artistName";
    static final String ARTIST_YEAR_FOUNDED = "artistFounded";
    static final String ARTIST_ALBUMS = "albumList";
    static final String ALBUM_NAME = ARTIST_ALBUMS + ".albumName";
    static final String ALBUM_SONGS_NUMBER = ARTIST_ALBUMS + ".albumSongsNumber";
    static final String ALBUM_YEAR_RELEASED = ARTIST_ALBUMS + ".albumYearReleased";
    static final String ALBUM_SONGS = ARTIST_ALBUMS + ".songsList";
    static final String SONGS_NAME = ALBUM_SONGS + ".songName";
    static final String SONGS_LENGTH = ALBUM_SONGS + ".songLength";
}
