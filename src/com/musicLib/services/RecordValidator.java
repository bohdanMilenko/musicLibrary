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


     boolean validateArtistAddMethod(Artist artist) throws ServiceException {
        validateRecordForNulls(artist);
        validateNoSuchArtistPresent(artist);
        return true;
    }

     boolean validateAlbumAddMethod(Album album) throws ServiceException {
        validateRecordForNulls(album);
        validateNoSuchAlbumPresent(album);
        validateIfNotNull(album.getArtist());
        validateArtistExistsAndUnique(album.getArtist());
        return true;
    }

     boolean validateSongAddMethod(Song song) throws ServiceException {
        validateRecordForNulls(song);
        validateNoSuchSongPresent(song);
        validateAlbumExistsAndUnique(song.getAlbum());
        return true;
    }

     boolean validateGetAlbumByArtist(Artist artist) throws ServiceException {
        validateRecordForNulls(artist);
        validateArtistExistsAndUnique(artist);
        return true;
    }

     boolean validateArtistDeleteMethod(Artist artist) throws ServiceException {
        validateRecordForNulls(artist);
        validateArtistExists(artist);
        return true;
    }

     boolean validateAlbumDeleteMethod(Album album) throws ServiceException {
        validateRecordForNulls(album);
        validateAlbumExists(album);
        return true;
    }

     boolean validateSongDeleteMethod(Song song) throws ServiceException {
        validateRecordForNulls(song);
        validateSongExists(song);
        return true;
    }


    private boolean validateRecordForNulls(Artist artist) throws ServiceException {
        validateIfNotNull(artist);
        return true;
    }

    private boolean validateRecordForNulls(Album album) throws ServiceException {
        validateIfNotNull(album);
        validateIfNotNull(album.getArtist());
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

    //TODO UPDATE ARTIST WITH ALBUMS WITHOUT RETURNING IT
    //WRITE ABOUT LAZY INIT
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
        if(foundArtists.size()==0){
            return true;

        }
        throw new DuplicatedRecordException();
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


    private boolean validateArtistExists(Artist artist) throws ServiceException {
        List<Artist> artists = artistService.getByName(artist);
        if  (artists.size() > 0) {
            return true;
        } else {
            throw new ArtistNotFoundException("There is no such artist");
        }
    }


    private boolean validateAlbumExists(Album album) throws ServiceException {
        List<Album> albums = albumService.get(album);
        if (albums.size() > 0) {
            return true;
        } else {
            throw new AlbumNotFoundException("There is no such album");
        }
    }

    private boolean validateSongExists(Song song) throws ServiceException {
        List<Song> songs = songService.get(song);
        if(songs.size() > 0) {
            return true;
        } else {
            throw new AlbumNotFoundException("There is no such song");
        }
    }

    void validateUpdateSongWithID(Song song) throws ServiceException {
        validateIfNotNull(song);
        validateIfNotNull(song.getAlbum());
        validateIfNotNull(song.getArtist());
    }



}
