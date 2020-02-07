package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.exceptions.QueryException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;

import java.sql.SQLException;
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

    //String and rename
    @Override
    public List<Album> get(Album album) throws ServiceException {
        try {
            return albumRepo.getByName(album.getName());
        } catch (SQLException e) {
            throw new ServiceException("Issue with db connectivity", e);
        }
    }


    //Put comments to describe complex methods
    @Override
    public List<Album> getByArtist(Artist artist) throws ServiceException {
        try {
            //todo if id > 0 do not check
            recordValidator.validateGetAlbumByArtist(artist);
            artist = artistService.updateArtistID(artist);
            return albumRepo.getAlbumsByArtistID(artist.getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable t get albums by artist", e);
        }
    }

    public boolean delete(Album album) throws ServiceException {
        try {
            recordValidator.validateAlbumDeleteMethod(album);
            updateAlbumWithID(album);
            updateAlbumWithArtistID(album);
            songService.deleteSongsFromAlbum(album);
            return albumRepo.delete(album.getId(), album.getArtist().getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable to delete album", e);
        }
    }

    private Album updateAlbumWithID(Album album) throws ServiceException {
        List<Album> foundAlbums = get(album);
        if (foundAlbums.size() == 1) {
            album.setId(foundAlbums.get(0).getId());
            return album;
        }
        throw new ServiceException("Unable to update album with ID from DB");

    }

    //TODO THIS METHOD SHOULD RETURN ALBUM, NOT SONG. UPDATING ARTIST ID SHOULD BE IN ARTIST SERVICE
    @Override
    public Song updateSongWithID(Song song) throws ServiceException {
        recordValidator.validateIfNotNull(song);
        updateSongWithAlbumID(song);
        updateSongWithArtistID(song);
        return song;
    }

    //TODO
    private Song updateSongWithAlbumID(Song song) throws ServiceException {
        try {
            Album albumFromSong = song.getAlbum();
            List<Album> foundAlbums = albumRepo.getByName(albumFromSong.getName());
            if (foundAlbums.size() == 1) {
                System.out.println("Album name: " + albumFromSong.getName());
                System.out.println("Album id fund in db: " + foundAlbums.get(0).getId() + ", " + foundAlbums.get(0).getName());
                albumFromSong.setId(foundAlbums.get(0).getId());
                song.setAlbum(albumFromSong);
                return song;
            } else {
                throw new QueryException("Either multiple or none albums with the same name");
            }
        } catch (SQLException e) {
            throw new ServiceException("Unable to update song with album ID", e);
        }
    }

    //TODO
    private Song updateSongWithArtistID(Song song) throws ServiceException {
        if (song.getAlbum().getId() != 0) {
            Artist artistFromSong = song.getArtist();
            recordValidator.validateIfNotNull(artistFromSong);
            List<Artist> artistFromDB = artistService.getByName(artistFromSong);
            int artistID = artistFromDB.get(0).getId();
            artistFromSong.setId(artistID);
            song.setArtist(artistFromSong);
            return song;
        } else {
            throw new QueryException("Either multiple or no artists found");
        }
    }

//    private Song updateSongWithArtistID(Song song) throws ServiceException {
//        if (song.getAlbum().getId() != 0) {
//            Album albumFromSong = song.getAlbum();
//            updateAlbumWithArtistID(albumFromSong);
//            song.setAlbum(albumFromSong);
//            return song;
//        } else {
//            throw new QueryException("Either multiple or no artists found");
//        }
//    }


    //ASK QUESTION: SHOULD I THROW EXCEPTION IN VALIDATION METHOD OR ALSO HAVE IF CONDITION IN THE METHOD?
    @Override
    public void deleteAlbumsFromArtist(Artist artist) throws ServiceException {
        try {
            if (recordValidator.validateIfNotNull(artist)) {
                removeDependantSongs(artist);
                albumRepo.deleteByArtistID(artist.getId());
            }
        } catch (QueryException e) {
            throw new ServiceException("Unable to delete dependant albums for artist: " + artist.getName(), e);
        }
    }

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
