package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ArtistNotFoundException;
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

    /**
     * Method to add new artist to DB.
     * Validation: if name of artist is not null && if such artist is not present id DB.
     * @param artist
     * @return List<Artist>
     * @throws ServiceException
     */
    public boolean add(Artist artist) throws ServiceException {
        try {
            recordValidator.validateArtistAddMethod(artist);
            return artistRepo.add(artist);
        } catch (SQLException e) {
            System.out.println(artist.toString());
            throw new ServiceException("Cannot insert artist to db", e);
        }
    }

    /**
     * Method to get all Artists from DB.
     * Validation: None.
     * @return List<Artist>
     * @throws ServiceException
     */
    public List<Artist> getAll() throws ServiceException {
        try {
            return artistRepo.getAll();
        } catch (SQLException e) {
            throw new ServiceException("Issue with getting all Artists", e);
        }
    }

    /**
     * Method to get Artist by name of the artist.
     * Validation: if name of artist is not null.
     * @param artist
     * @return List<Artist>
     * @throws ServiceException
     */
    public List<Artist> getByName(Artist artist) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(artist);
            List<Artist> artists = artistRepo.getByName(artist.getName());
            return artists;
        } catch (SQLException e) {
            throw new ServiceException("Failed to get Artist by Name", e);
        }
    }

    /**
     * Method that removes Artist from the DB.
     * Validation: if name of artist is not null && such artist truly present in DB.
     *
     * Artist may have dependant Albums and Songs, so CASCADE REMOVAL is implemented:
     * Checks if Artist has albums in albums DB.
     * If so -> checks if each album has dependant songs -> if there any, they are deleted.
     * Afterwards album is deleted.
     * When all albums and songs deleted -> artist deleted as well.
     * @param artist
     * @return boolean
     * @throws ServiceException
     */
    public boolean delete(Artist artist) throws ServiceException {
        try {
            recordValidator.validateArtistDeleteMethod(artist);
            artist = updateArtistID(artist);
            if (recordValidator.hasDependantAlbums(artist)) {
                System.out.println("Artist has dependant albums");
                artist = updateAlbumsForArtist(artist);
                albumService.deleteAlbumsForArtist(artist);
            }
            return artistRepo.delete(artist.getName());
        } catch (ArtistNotFoundException | SQLException e) {
            throw new ServiceException("Unable to delete artist: " + artist.getName(), e);
        }
    }

    /**
     * Supporting method to the method above.
     * Retrieves all the albums from DB for the passed Artist.
     * @param artist
     * @return Artist
     * @throws ServiceException
     */
    private Artist updateAlbumsForArtist(Artist artist) throws ServiceException {
        try {
            List<Album> foundAlbums = albumService.getByArtist(artist);
            artist.setAlbums(foundAlbums);
            return artist;
        } catch (ServiceException e) {
            throw new ServiceException("Unable to update artist with albums", e);
        }
    }

    /**
     * Method is used to retrieve ArtistID from DB, to have a complete entity.
     * @param artist
     * @return
     * @throws ServiceException
     */
    public Artist updateArtistID(Artist artist) throws ServiceException {
        if(artist.getId() == 0) {
            List<Artist> foundArtists = getByName(artist);
            int artistId = foundArtists.get(0).getId();
            artist.setId(artistId);
        }
        return artist;
    }


    public void setAlbumService(AlbumService albumService) {
        this.albumService = albumService;
    }

    public void setRecordValidator(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }
}

