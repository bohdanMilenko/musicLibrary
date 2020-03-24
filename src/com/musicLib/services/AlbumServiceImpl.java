package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumServiceImpl implements AlbumService {

    private AlbumRepository albumRepo;
    private ArtistService artistService;
    private SongService songService;
    private RecordValidator recordValidator;

    public AlbumServiceImpl(AlbumRepository albumRepo) {
        this.albumRepo = albumRepo;
    }

    public AlbumServiceImpl() {
    }

    /**
     * Method to add a new album to DB.
     * Validation: if passed album and Artist are not null && if album with such name is not present in DB &&
     * if artist with such name exists in DB (artist should be unique).
     *
     * @param album
     * @return
     * @throws ServiceException
     */
    public boolean add(Album album) throws ServiceException {
        try {
            recordValidator.validateAlbumAddMethod(album);
            album = updateAlbumWithArtistID(album);
            return albumRepo.add(album);
        } catch (SQLException e) {
            throw new ServiceException("Issue with adding album to db", e);
        }
    }

    private Album updateAlbumWithArtistID(Album album) throws ServiceException {
        Artist artistFromAlbum = album.getArtist();
        List<Artist> foundArtists = artistService.getByName(album.getArtist());
        artistFromAlbum.setId(foundArtists.get(0).getId());
        album.setArtist(artistFromAlbum);
        return album;
    }


    @Override
    public List<Album> get(Album album) throws ServiceException {
        try {
            return albumRepo.getByName(album.getName());
        } catch (SQLException e) {
            throw new ServiceException("Issue with db connectivity", e);
        }
    }


    /**
     * Method that returns albums by artist Name
     * Validation: validates if passed artist is not Null && if this artist exists in DB
     * Retrieves albums by ID -> so performs query to Artists table to update id of Artist entity
     *
     * @param artist
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Album> getByArtist(Artist artist) throws ServiceException {
        try {
            recordValidator.validateGetAlbumByArtist(artist);
            if (artist.getId() <= 0) {
                artist = artistService.updateArtistID(artist);
            }
            return albumRepo.getAlbumsByArtistID(artist.getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable to get albums by artist:\n\t" + artist.toString(), e);
        }
    }

    /**
     * Performs Cascade deletion of Album and songs with dependencies oon this Album
     * Validation: if passed album and artist (enclosed) objects are not Nulls && if such album exists in DB.
     * Updates passed object with ids retrieved from db for Album and enclosed Artist.
     * If Albums has dependant Songs -> deletes them.
     *
     * @param album
     * @return
     * @throws ServiceException
     */
    public boolean delete(Album album) throws ServiceException {
        try {
            recordValidator.validateAlbumDeleteMethod(album);
            updateAlbumWithID(album);
            updateAlbumWithArtistID(album);
            List<Album> albumList = new ArrayList<>();
            albumList.add(album);
            if (recordValidator.hasDependantSongs(albumList)) {
                songService.deleteSongsFromAlbum(album);
            }
            return albumRepo.delete(album.getId(), album.getArtist().getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable to delete album", e);
        }
    }

    /**
     * Updates and returns passed Album with id from DB.
     * Validation: If Album is not Null.
     * If there is no Album with such name or more that one Album with the same name,
     * the exception will be thrown
     *
     * @param album
     * @return
     * @throws ServiceException
     */
    @Override
    public Album updateAlbumWithID(Album album) throws ServiceException {
        recordValidator.validateIfNotNull(album);
        List<Album> foundAlbums = get(album);
        if (foundAlbums.size() == 1) {
            album.setId(foundAlbums.get(0).getId());
            return album;
        }
        throw new ServiceException("Unable to update album with ID from DB");
    }

    /**
     * Updates passed song with IDs from db for enclosed Album and Artist.
     * Validation: If Song and enclosed Album and Artists are not Nulls.
     * Updates enclosed Album and Artists with IDs from Db.
     *
     * @param song
     * @return
     * @throws ServiceException
     */
    @Override
    public Song updateSongWithID(Song song) throws ServiceException {
        recordValidator.validateRecordForNulls(song);
        updateSongWithAlbumID(song);
        updateSongWithArtistID(song);
        return song;
    }

    /**
     * Supporting Method for updateSongWithID: Updates Song with Album ID
     * If ID in passed object > 0 - returns the same object (object already has a valid ID)
     * Validation: requires to have one Album with the same name in DB, otherwise exception is thrown.
     *
     * @param song
     * @return
     * @throws ServiceException
     */
    private Song updateSongWithAlbumID(Song song) throws ServiceException {
        Album albumFromSong = song.getAlbum();
        recordValidator.validateIfNotNull(albumFromSong);
        if (albumFromSong.getId() <= 0) {
            List<Album> foundAlbums = get(albumFromSong);
            if (foundAlbums.size() == 1) {
                albumFromSong.setId(foundAlbums.get(0).getId());
                song.setAlbum(albumFromSong);
                return song;
            } else {
                throw new QueryException("Unable to update Songs with ids: Either multiple or none albums with the same name:\n" +
                        song.toString());
            }
        }
        return song;
    }

    /**
     * Supporting Method for updateSongWithID: Updates Song with Artist ID
     * If ID in passed object > 0 - returns the same object (object already has a valid ID)
     * Validation: requires to have one Artist with the same name in DB, otherwise exception is thrown.
     *
     * @param song
     * @return
     * @throws ServiceException
     */
    private Song updateSongWithArtistID(Song song) throws ServiceException {
        Artist artistFromSong = song.getArtist();
        recordValidator.validateIfNotNull(artistFromSong);
        if (artistFromSong.getId() <= 0) {
            List<Artist> artistFromDB = artistService.getByName(artistFromSong);
            if (artistFromDB.size() == 1) {
                int artistID = artistFromDB.get(0).getId();
                artistFromSong.setId(artistID);
                song.setArtist(artistFromSong);
                return song;
            } else {
                throw new QueryException("Either multiple or no artists found");
            }
        }
        return song;
    }

    /**
     * Supporting method for Artist class - delete()
     * Validation: if artist is not Null &&  if passed artist has an ID.
     * Invokes method that deletes Songs for passed Artist (must have non-empty list of Albums)
     * Then removes all Albums for passed Artist
     *
     * @param artist
     * @return
     * @throws ServiceException
     */
    @Override
    public boolean deleteAlbumsForArtist(Artist artist) throws ServiceException {
        try {
            recordValidator.validateIfNotNull(artist);
            if (artist.getId() > 0) {
                removeDependantSongs(artist);
                albumRepo.deleteByArtistID(artist.getId());
                return true;
            }
            throw new ServiceException("Artist has empty id: " + artist.toString());
        } catch (QueryException e) {
            throw new ServiceException("Unable to delete dependant albums for artist: " + artist.getName(), e);
        }
    }

    /**
     * Deletes songs for each Album
     * Validation: if passed album list is not empty (cuz ArtistService is supposed to verify if artist has dependant albums
     * if code reaches this method, Artist MUST have albums. So if albums are empty - means that they were not updated from DB)
     *
     * @param artist
     * @throws ServiceException
     */
    private void removeDependantSongs(Artist artist) throws ServiceException {
        List<Album> albums = artist.getAlbums();
        if (albums.isEmpty()) {
            throw new AlbumNotFoundException("No albums to delete from");
        }
        try {
            if (recordValidator.hasDependantSongs(albums)) {
                for (Album album : albums) {
                    songService.deleteSongsFromAlbum(album);
                }
            }
        } catch (ServiceException e) {
            throw new ServiceException("Unable to delete songs from albums", e);
        }
    }

    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    public void setSongService(SongService songService) {
        this.songService = songService;
    }

    public void setRecordValidator(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }
}
