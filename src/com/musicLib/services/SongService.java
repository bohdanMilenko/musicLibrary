package com.musicLib.services;

import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;

import java.util.List;

public interface SongService {

    boolean add(Song song) throws  ServiceException;

    List<Song> getByName(Song song) throws ServiceException;

    boolean delete(Song song) throws ServiceException;

    void setAlbumService(AlbumService albumService);

    void setRecordValidator(RecordValidator recordValidator);


}
