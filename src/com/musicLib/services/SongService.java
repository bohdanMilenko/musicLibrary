package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;

import java.util.List;

public interface SongService {

    boolean add(Song song) throws  ServiceException;

    List<Song> get(Song song) throws ServiceException;

    //TODO ADD METHOD TO VALIDATE IF ALBUM/ARTIST HAS DEPENDANT SONGS

    boolean delete(Song song) throws ServiceException;

    void setAlbumService(AlbumService albumService);

    void setRecordValidator(RecordValidator recordValidator);

    void deleteSongsFromAlbum(Album album) throws ServiceException;
}
