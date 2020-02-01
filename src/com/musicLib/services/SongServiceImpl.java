package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.*;
import com.musicLib.repository.SongRepository;

import java.sql.SQLException;
import java.util.List;

public class SongServiceImpl implements SongService {

    private SongRepository songRepo;
    private RecordValidator recordValidator;

    public SongServiceImpl(SongRepository songRepo, RecordValidator recordValidator) {
        this.songRepo = songRepo;
        this.recordValidator = recordValidator;
    }

    public SongServiceImpl() {
    }

    public boolean add(Song song) throws ServiceException {
        Artist artistFromSong = song.getArtist();
        Album albumFromSong = song.getAlbum();
        int artistId = getArtistIDFromDB(artistFromSong);
        int albumId = getAlbumIDFromDB(albumFromSong);
        Song updatedSong = updateArtistWithID(song, artistId);
        updatedSong = updateWithAlbumID(updatedSong, albumId);
        try {
            return songRepo.add(updatedSong);
        } catch (SQLException e) {
            throw new ServiceException("Unable to add song", e);
        }
    }

    private int getArtistIDFromDB(Artist artist) throws ServiceException {
        int idToReturn;
        try {
            idToReturn = recordValidator.validateArtist(artist);
            return idToReturn;
        } catch (QueryException e) {
            throw new ServiceException("Cannot get Artist ID", e);
        }

    }

    private int getAlbumIDFromDB(Album album) throws ServiceException {
        int idToReturn;
        try {
            idToReturn = recordValidator.getAlbumID(album);
            return idToReturn;
        } catch (QueryException e) {
            throw new ServiceException("Cannot get Album ID", e);
        }
    }

    private Song updateArtistWithID(Song song, int artistId) {
        Artist artist = song.getArtist();
        artist.setId(artistId);
        song.setArtist(artist);
        return song;
    }

    private Song updateWithAlbumID(Song song, int albumId) {
        Album album = song.getAlbum();
        album.setId(albumId);
        song.setAlbum(album);
        return song;
    }


    public List<Song> getByName(String songName) throws ServiceException {
        try {
            return songRepo.queryByName(songName);
        } catch (SQLException e) {
            throw new ServiceException("Cannot find song by it's name", e);
        }
    }

    public boolean delete(String artistName, String albumName, String songName) throws ServiceException {
       try {
           int albumID = recordValidator.getAlbumID(albumName);
           return songRepo.delete(songName, albumID);
       }catch (SQLException e){
           throw new ServiceException("Cannot delete song from DB", e);
       }
    }

    public boolean delete(String artistName, String albumName) throws ServiceException {
        try {
            int artistID = recordValidator.validateArtist(artistName);
            int albumID = recordValidator.getAlbumID(albumName);
            return songRepo.deleteByAlbumId(albumID);
        }catch (SQLException e){
            throw new ServiceException("Cannot delete song from DB", e);
        }
    }




}
