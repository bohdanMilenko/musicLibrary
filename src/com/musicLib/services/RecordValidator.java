package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.exceptions.ServiceException;

import java.util.List;

public class RecordValidator {

    private ArtistService artistService;
    private AlbumService albumService;
    private SongService songService;

    public RecordValidator(ArtistService artistService, AlbumService albumService, SongService songService) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
    }


    public boolean validateArtistAddMethod(Artist artist) throws ServiceException {
        validateRecordForNulls(artist);
        validateNoSuchArtistPresent(artist);
        return true;
    }

    public boolean validateAlbumAddMethod(Album album) throws ServiceException {
        validateRecordForNulls(album);
        validateNoSuchAlbumPresent(album);
        validateArtistExistsAndUnique(album.getArtist());
        return true;
    }

    public boolean validateSongAddMethod(Song song) throws ServiceException {
        validateRecordForNulls(song);
        validateNoSuchSongPresent(song);
        validateAlbumExistsAndUnique(song.getAlbum());
        return true;
    }

    public boolean validateGetAlbumByArtist(Artist artist) throws ServiceException{
        validateRecordForNulls(artist);
        validateArtistExistsAndUnique(artist);
        return true;
    }

    public boolean validateArtistDeleteMethod(Artist artist) throws ServiceException {
        validateRecordForNulls(artist);
        validateArtistExistsAndUnique(artist);
        return true;
    }

    public boolean validateAlbumDeleteMethod(Album album) throws ServiceException {
        validateRecordForNulls(album);
        validateAlbumExistsAndUnique(album);
        return true;
    }

    public boolean validateSongDeleteMethod(Song song) throws ServiceException {
        validateRecordForNulls(song);
        validateSongExistsAndUnique(song);
        return true;
    }


    private boolean validateRecordForNulls(Artist artist) throws ServiceException {
        validateIfNotNull(artist);
        return true;
    }

    private boolean validateRecordForNulls(Album album) throws ServiceException {
        validateIfNotNull(album);
        validateIfNotNull(album.getArtist());
        validateNoSuchAlbumPresent(album);
        return true;
    }

    private boolean validateRecordForNulls(Song song) throws ServiceException {
        validateIfNotNull(song);
        validateIfNotNull(song.getArtist());
        validateIfNotNull(song.getAlbum());
        return true;
    }

    private boolean validateNoSuchAlbumPresent(Album album) throws ServiceException {
        List<Album> albums = albumService.get(album);
        if (albums.size() == 0) {
            return true;
        }
        throw new DuplicatedRecordException("Such album already exists");
    }

    private boolean validateNoSuchSongPresent(Song song) throws ServiceException {
        List<Song> songs = songService.get(song);
        if (songs.size() == 0) {
            return true;
        }
        throw new DuplicatedRecordException("Such song already exists");
    }

    public boolean hasDependantAlbums(Artist artist) throws ServiceException {
        List<Album> dependantAlbums = albumService.getByArtist(artist);
        return dependantAlbums.size() > 0;
    }

    public boolean hasDependantSongs(List<Album> albums) throws ServiceException {
        for (Album album : albums) {
            List<Song> dependantSongs = songService.getByAlbum(album);
            if (dependantSongs.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean validateNoSuchArtistPresent(Artist artist) throws ServiceException {
        List<Artist> foundArtists = artistService.getByName(artist);
        return foundArtists.size() <= 0;
    }


    public boolean validateIfNotNull(Object entity) throws ServiceException {
        if (entity == null) {
            throw new ServiceException("Passed object is null");
        }
        return true;
    }


    private boolean validateArtistExistsAndUnique(Artist artist) throws ServiceException {
        List<Artist> artists = artistService.getByName(artist);
        if (artists.size() == 1) {
            return true;
        } else if (artists.size() > 1) {
            throw new DuplicatedRecordException("More than one artist with the same name");
        } else {
            throw new ArtistNotFoundException("There is no such artist");
        }
    }


    private boolean validateAlbumExistsAndUnique(Album album) throws ServiceException {
        List<Album> albums = albumService.get(album);
        if (albums.size() == 1) {
            return true;
        } else if (albums.size() > 1) {
            throw new DuplicatedRecordException("More than one album with the same name");
        } else {
            throw new AlbumNotFoundException("There is no such album");
        }
    }

    private boolean validateSongExistsAndUnique(Song song) throws ServiceException {
        List<Song> songs = songService.get(song);
        if (songs.size() == 1) {
            return true;
        } else if (songs.size() > 1) {
            throw new DuplicatedRecordException("More than one song with the same name");
        } else {
            throw new AlbumNotFoundException("There is no such song");
        }
    }

}
