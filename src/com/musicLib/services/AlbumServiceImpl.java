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
            if (validateAlbum(album)) {
                updateAlbumWithArtistID(album);
                return albumRepo.add(album);
            }
            throw new ServiceException("Failed to add Album");
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

    private boolean validateAlbum(Album album) throws ServiceException {
        return recordValidator.validateAlbum(album);
    }

    public List<Album> get(Album album) throws ServiceException {
        try {
            return albumRepo.getByName(album.getName());
        } catch (SQLException e) {
            throw new ServiceException("Issue with db connectivity", e);
        }
    }

    @Override
    public List<Album> getByArtist(Artist artist) throws ServiceException {
        try {
            artist = artistService.updateArtistID(artist);
            return albumRepo.getAlbumsByArtistID(artist.getId());
        } catch (SQLException e) {
            throw new ServiceException("Unable t get albums by artist", e);
        }
    }

    public boolean delete(Album album) throws ServiceException {
        try {
            if (recordValidator.validateAlbum(album)) {
                updateAlbumWithID(album);
                updateAlbumWithArtistID(album);
                songService.deleteSongsFromAlbum(album);
                return albumRepo.delete(album.getId(), album.getArtist().getId());
            }
            throw new ServiceException("Unable to delete album");
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


    @Override
    public Song updateSongWithID(Song song) throws ServiceException {
        updateSongWithAlbumID(song);
        updateSongWithArtistID(song);
        return song;
    }


    private Song updateSongWithAlbumID(Song song) throws ServiceException {
        try {
            Album albumFromSong = song.getAlbum();
            List<Album> foundAlbums = albumRepo.getByName(albumFromSong.getName());
            if (foundAlbums.size() == 1) {
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

    private Song updateSongWithArtistID(Song song) throws ServiceException {
        if (song.getAlbum().getId() != 0) {
            Album albumFromSong = song.getAlbum();
            updateAlbumWithArtistID(albumFromSong);
            song.setAlbum(albumFromSong);
            return song;
        } else {
            throw new QueryException("Either multiple or no artists found");
        }
    }

    @Override
    public void deleteAlbumsFromArtist(Artist artist) throws ServiceException {
        try {
            removeDependantSongs(artist);
            albumRepo.deleteByArtistID(artist.getId());
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
