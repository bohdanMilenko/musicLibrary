package com.musicLib.MongoDBRepisotory;

public class MetaDataMongo {

    public static final String ARTIST_NAME = "artistName";
    public static final String ARTIST_YEAR_FOUNDED = "artistFounded";
    public static final String ARTIST_GENRE = "artistGenre";
    public static final String ARTIST_ALBUMS = "albumList";
    public static final String ALBUM_NAME = ARTIST_ALBUMS + ".albumName";
    public static final String ALBUM_SONGS_NUMBER = ARTIST_ALBUMS + ".albumSongsNumber";
    public static final String ALBUM_YEAR_RELEASED = ARTIST_ALBUMS +".albumYearReleased";
    public static final String ALBUM_SONGS = ARTIST_ALBUMS + ".songsList";
    public static final String SONGS_NAME = ALBUM_SONGS + ".songName";
    public static final String SONGS_LENGTH = ALBUM_SONGS +".songLength";
}
