package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.exceptions.ArtistNotFoundException;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.exceptions.QueryException;

import java.util.List;

public class RecordValidator {

    private ArtistService artistService;
    private AlbumService albumService;
    private SongService songService;

    public RecordValidator(ArtistService artistService, AlbumService albumService) {
        this.artistService = artistService;
        this.albumService = albumService;
    }

    public RecordValidator(ArtistService artistService) {
        this.artistService = artistService;
    }


    public int getArtistID(Artist artist) throws QueryException {
        return getArtistID(artist.getName());
    }

    public int getArtistID(String artistName) throws QueryException {
        List<Artist> artists = artistService.getByName(artistName);
        if (artists.size() == 1) {
            Artist foundArtist = artists.get(0);
            return foundArtist.getId();
        } else if (artists.size() > 1) {
            throw new DuplicatedRecordException("More than one artist with the same name");
        } else {
            throw new ArtistNotFoundException("There is no such artist");
        }
    }

    public int getAlbumID(Album album) throws QueryException {
       return getAlbumID(album.getName());
    }

    public int getAlbumID(String  album) throws QueryException {
        List<Album> albums = albumService.getByName(album);
        if (albums.size() == 1) {
            Album foundAlbums = albums.get(0);
            return foundAlbums.getId();
        } else if (albums.size() > 1) {
            throw new DuplicatedRecordException("More than one album with the same name");
        } else {
            throw new AlbumNotFoundException("There is no such album");
        }
    }

//    public boolean ensureDependantSongsDeleted(int albumID) throws QueryException{
//
//    }
}
