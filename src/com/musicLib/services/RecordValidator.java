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

    //Idea to ask: pass here 3 references with repositories. When artist is queried, it will be returned as artist with no album list. from
    //Artist service, i call method from recordValidator.UpdateArtistWithAlbums and Record Validator uses album repo to get list of albums.
    //They are added to artist, but we need also Songs for album. Record validator then call method
    // for each album: recordValidator.UpdateAlbumWithSongs and then albums are updated and Artist is returned.

    //Can I work with Repositories in this class instead of Services?

    private ArtistService artistService;
    private AlbumService albumService;
    private SongService songService;

    public RecordValidator(ArtistService artistService, AlbumService albumService, SongService songService) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
    }

    boolean validateArtist(Artist artist) throws ServiceException {
        List<Artist> artists = artistService.getByName(artist);
        if (artists.size() == 1) {
            return true;
        } else if (artists.size() > 1) {
            throw new DuplicatedRecordException("More than one artist with the same name");
        } else {
            throw new ArtistNotFoundException("There is no such artist");
        }
    }


    public boolean validateAlbum(Album album) throws ServiceException {
        List<Album> albums = albumService.get(album);
        if (albums.size() == 1) {
            return true;
        } else if (albums.size() > 1) {
            throw new DuplicatedRecordException("More than one album with the same name");
        } else {
            throw new AlbumNotFoundException("There is no such album");
        }
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
}
