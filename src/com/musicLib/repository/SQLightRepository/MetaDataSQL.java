package com.musicLib.repository.SQLightRepository;

public class MetaDataSQL {

    static final String TABLE_ALBUMS = "albums";
    static final String COLUMN_ALBUMS_ID = "_id";
    static final String COLUMN_ALBUMS_NAME = "name";
    static final String COLUMN_ALBUMS_ARTIST = "artist";
    static final int INDEX_ALBUMS_ID = 1;
    static final int INDEX_ALBUMS_NAME = 2;
    static final int INDEX_ALBUMS_ARTIST = 3;

    static final String TABLE_SONGS = "songs";
    static final String COLUMN_SONGS_ID = "_id";
    static final String COLUMN_SONGS_TITLE = "title";
    static final String COLUMN_SONGS_TRACK = "track";
    static final String COLUMN_SONGS_ALBUM = "album";
    static final int INDEX_SONGS_ID = 1;
    static final int INDEX_SONGS_TRACK = 2;
    static final int INDEX_SONGS_TITLE = 3;
    static final int INDEX_SONGS_ALBUM = 4;

    static final String TABLE_ARTISTS = "artists";
    static final String COLUMN_ARTISTS_ID = "_id";
    static final String COLUMN_ARTISTS_NAME = "name";
    static final int INDEX_ARTISTS_ID = 1;
    static final int INDEX_ARTISTS_NAME = 2;

    static final int ORDER_NONE = 1;
    static final int ORDER_ASC = 2;
    static final int ORDER_DESC = 3;


}
