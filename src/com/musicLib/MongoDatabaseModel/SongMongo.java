package com.musicLib.MongoDatabaseModel;

public class SongMongo {

    private String songName;
    private String songLength;


    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongLength() {
        return songLength;
    }

    public void setSongLength(String songLength) {
        this.songLength = songLength;
    }
}
