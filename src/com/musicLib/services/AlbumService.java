package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;

import java.util.List;

public interface AlbumService {

    boolean add(Album album) throws ServiceException;

    List<Album> get(Album album) throws ServiceException;

    //TODO ADD SEARCH BY ARTIST TO VALIDATE IF ARTIST HAS DEPENDANT ALBUMS

    boolean delete(Album album) throws ServiceException;

    Song updateSongWithID(Song song) throws ServiceException;
    
    void setArtistService(ArtistService artistService);
    
    void setSongService(SongService songService);
    
    void setRecordValidator(RecordValidator recordValidator);

    void deleteAlbumsFromArtist(Artist artist) throws ServiceException;
}
