package com.musicLib.repository.MongoDBRepisotory;

public class MetaDataMongo {

    static final String ALBUMS_COLLECTION = "albums";
    static final String ARTISTS_COLLECTION = "artists";

    static final String ARTIST_ID = "_id";
    static final String ARTIST_NAME = "artistName";
    static final String ARTIST_YEAR_FOUNDED = "artistFounded";
    static final String ARTIST_ALBUMS_LIST = "albumList";

    static final String ARTIST_ALBUM_ID = ARTIST_ALBUMS_LIST + "._id";
    static final String ARTIST_ALBUM_NAME = ARTIST_ALBUMS_LIST + ".albumName";


    static final String ALBUM_ID = "_id";
    static final String ALBUM_NAME = "albumName";
    static final String ALBUM_SONGS_LIST = "songsList";
    static final String SONG_NAME = ALBUM_SONGS_LIST + ".songName";
    static final String SONG_NUMBER = ALBUM_SONGS_LIST + ".trackNumber";





    static final String ALBUM_SONGS_NUMBER = ARTIST_ALBUMS_LIST + ".albumSongsNumber";
    static final String ALBUM_YEAR_RELEASED = ARTIST_ALBUMS_LIST + ".albumYearReleased";
    static final String ALBUM_SONGS = ARTIST_ALBUMS_LIST + ".songsList";
    static final String SONGS_NAME = ALBUM_SONGS + ".songName";
    static final String SONGS_LENGTH = ALBUM_SONGS + ".songLength";
}
