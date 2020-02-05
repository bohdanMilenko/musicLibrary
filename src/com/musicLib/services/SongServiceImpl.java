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


    public List<Song> get(Song song) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(song);
            return songRepo.getByName(song.getName());
        } catch (SQLException e) {
            throw new ServiceException("Cannot find song by it's name", e);
        }
    }

    @Override
    public List<Song> getByAlbum(Album album) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(album);
            return songRepo.getByAlbumId(album.getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable get songs by album", e);
        }
    }

    public boolean delete(Song song) throws ServiceException {
        try {
            recordValidator.validateSongDeleteMethod(song);
            updateSongWithRequiredID(song);
            return songRepo.delete(song);
        } catch (SQLException e) {
            throw new ServiceException("Cannot delete song from DB", e);
        }
    }

    @Override
    public void deleteSongsFromAlbum(Album album) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(album);
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
