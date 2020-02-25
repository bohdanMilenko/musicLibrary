package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.SongRepository;

import java.sql.SQLException;
import java.util.List;

public class SongServiceImpl implements SongService {

    private SongRepository songRepo;
    private AlbumService albumService;
    private RecordValidator recordValidator;

    public SongServiceImpl(SongRepository songRepo) {
        this.songRepo = songRepo;
    }

    public SongServiceImpl() {
    }

    /**
     * Method to add song to DB.
     * Validation: passed song object along with enclosed Artist and Album are not Nulls &&
     * If required album present in DB && if song with such name doesn't exist in DB.
     * Updates enclosed Artist and Album with IDs from db
     *
     * @param song
     * @return
     * @throws ServiceException
     */
    public boolean add(Song song) throws ServiceException {
        try {
            recordValidator.validateSongAddMethod(song);
            song = updateSongWithRequiredID(song);
            return songRepo.add(song);
        } catch (SQLException e) {
            throw new ServiceException("Unable to add song", e);
        }
    }


    private Song updateSongWithRequiredID(Song song) throws ServiceException {
        return albumService.updateSongWithID(song);
    }

    /**
     * Method to get song from DB based on it's name
     * Validation: if passed object is not Null
     *
     * @param song
     * @return
     * @throws ServiceException
     */
    public List<Song> get(Song song) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(song);
            return songRepo.getByName(song.getName());
        } catch (SQLException e) {
            throw new ServiceException("Cannot find song by it's name", e);
        }
    }

    /**
     * Method to get all songs for a certain Album
     * Validation: if passed object is not Null
     * Updates Album with IDs from db
     * Retrieves songs by Album ID
     *
     * @param album
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Song> getByAlbum(Album album) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(album);
            albumService.updateAlbumWithID(album);
            return songRepo.getByAlbumId(album.getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable get songs by album", e);
        }
    }


    /**
     * Deletes song from songs table.
     * Song name && artists' id && albums' IDs are required.
     * Validation: if passed song and enclosed Artist & Album are not Nulls.
     * Updates enclosed Artist and Album with IDs from db
     *
     * @param song
     * @return
     * @throws ServiceException
     */
    public boolean delete(Song song) throws ServiceException {
        try {
            recordValidator.validateRecordForNulls(song);
            updateSongWithRequiredID(song);
            return songRepo.delete(song);
        } catch (SQLException e) {
            throw new ServiceException("Cannot delete song from DB", e);
        }
    }

    /**
     * Deletes songs from songs table by Album ID.
     * Albums' ID is required.
     * Validation: if Album is not Null.
     * Updates Album with IDs from db
     * Deletes songs by album ID
     * @param album
     * @throws ServiceException
     */
    @Override
    public void deleteSongsFromAlbum(Album album) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(album);
            albumService.updateAlbumWithID(album);
            songRepo.deleteByAlbumId(album.getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable to delete songs from album", e);
        }
    }

    public void setAlbumService(AlbumService albumService) {
        this.albumService = albumService;
    }

    public void setRecordValidator(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }
}
