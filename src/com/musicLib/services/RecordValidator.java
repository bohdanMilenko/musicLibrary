package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.*;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.SongRepository;

import java.sql.SQLException;
import java.util.List;

public class RecordValidator {

    //Idea to ask: pass here 3 references with repositories. When artist is queried, it will be returned as artist with no album list. from
    //Artist service, i call method from recordValidator.UpdateArtistWithAlbums and Record Validator uses album repo to get list of albums.
    //They are added to artist, but we need also Songs for album. Record validator then call method
    // for each album: recordValidator.UpdateAlbumWithSongs and then albums are updated and Artist is returned.

    //Can I work with Repositories in this class instead of Services?

    private ArtistRepository artistRepository;
    private AlbumRepository albumRepository;
    private SongRepository songRepository;

    public RecordValidator(ArtistRepository artistRepository, AlbumRepository albumRepository, SongRepository songRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    public int getArtistID(Artist artist) throws QueryException {
        return getArtistID(artist.getName());
    }

    public int getArtistID(String artistName) throws QueryException {
        List<Artist> artists = artistRepository.queryArtist(artistName);
        if (artists.size() == 1) {
            Artist foundArtist = artists.get(0);
            return foundArtist.getId();
        } else if (artists.size() > 1) {
            throw new DuplicatedRecordException("More than one artist with the same name");
        } else {
            throw new ArtistNotFoundException("There is no such artist");
        }
    }

    public int getAlbumID(Album album) throws ServiceException {
            return getAlbumID(album.getName());
    }

    public int getAlbumID(String album) throws ServiceException {
        List<Album> albums;
        try {
            albums = albumRepository.queryByName(album);
        }catch (SQLException e){
            throw new ServiceException("Unable to get album ID", e);
        }
        if (albums.size() == 1) {
            Album foundAlbums = albums.get(0);
            return foundAlbums.getId();
        } else if (albums.size() > 1) {
            throw new DuplicatedRecordException("More than one album with the same name");
        } else {
            throw new AlbumNotFoundException("There is no such album");
        }
    }


    public List<Artist> addAlbumsToArtist(List<Artist> artists) throws ServiceException{
        try {
            for (Artist tempArtist : artists) {
                List<Album> tempAlbums = albumRepository.queryAlbumsByArtistID(tempArtist.getId());
                if (tempAlbums.size() > 0) {
                    tempAlbums = addSongsToAlbum(tempAlbums);
                }
                tempArtist.setAlbums(tempAlbums);
            }
            return artists;
        }catch (SQLException e){
            throw new ServiceException("Unable to add albums to artists", e);
        }

    }

    public List<Album> addSongsToAlbum(List<Album> albums) throws ServiceException{
        try {
            for (Album tempAlbum : albums) {
                List<Song> tempSongs = songRepository.queryByAlbumId(tempAlbum.getId());
                tempAlbum.setSongs(tempSongs);
            }
            return albums;
        }catch (SQLException e){
            throw new ServiceException("Unable to add songs to Albums", e);
        }
    }

    public boolean deleteDependantAlbums(Artist artist) throws ServiceException{

    }
}
