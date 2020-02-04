package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.ArtistRepository;

import java.sql.SQLException;
import java.util.List;

public class ArtistServiceImpl implements ArtistService {

    private ArtistRepository artistRepo;
    private AlbumService albumService;
    private RecordValidator recordValidator;

    public ArtistServiceImpl(ArtistRepository artistRepo) {
        this.artistRepo = artistRepo;
    }

    public ArtistServiceImpl() {
    }

    public boolean add(Artist artist) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(artist);
            recordValidator.validateNoSuchArtistPresent(artist);
            return artistRepo.add(artist);
        } catch (SQLException e) {
            throw new ServiceException("Cannot insert artist to db", e);
        }
    }

    public List<Artist> getAll() throws ServiceException {
        try {
            List<Artist> artists = artistRepo.getAll();
            return artists;
        } catch (SQLException e) {
            throw new ServiceException("Issue with getting all Artists", e);
        }
    }

    public List<Artist> getByName(Artist artist) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(artist);
            List<Artist> artists = artistRepo.getByName(artist.getName());
            return artists;
        } catch (SQLException e) {
            throw new ServiceException("Issue with getting all Artists", e);
        }
    }


    //FINISHED
    public boolean delete(Artist artist) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(artist);
            artist = updateArtistID(artist);
            if (recordValidator.hasDependantAlbums(artist)) {
                artist = updateAlbums(artist);
                albumService.deleteAlbumsFromArtist(artist);
            }
            return artistRepo.delete(artist.getName());
        } catch (SQLException e) {
            throw new ServiceException("Unable to delete artist: " + artist.getName(), e);
        }
    }

    public Artist updateArtistID(Artist artist) throws ServiceException {
        recordValidator.validateIfNotNull(artist);
        if (recordValidator.validateArtist(artist)) {
            List<Artist> foundArtists = getByName(artist);
            int artistId = foundArtists.get(0).getId();
            artist.setId(artistId);
            return artist;
        }
        throw new ServiceException("Unable to update Artist with ID");
    }

    Artist updateAlbums(Artist artist) throws ServiceException {
        try {
            List<Album> foundAlbums = albumService.getByArtist(artist);
            artist.setAlbums(foundAlbums);
            return artist;
        } catch (ServiceException e) {
            throw new ServiceException("Unable to update artist with albums", e);
        }
    }


    public void setAlbumService(AlbumService albumService) {
        this.albumService = albumService;
    }

    public void setRecordValidator(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }
}

