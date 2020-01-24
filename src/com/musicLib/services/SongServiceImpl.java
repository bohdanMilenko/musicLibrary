package com.musicLib.services;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.DuplicatedRecordException;
import com.musicLib.exceptions.NotFoundException;
import com.musicLib.repository.SongRepository;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.exceptions.ArtistNotFoundException;

import javax.xml.validation.Validator;
import java.sql.SQLException;
import java.util.List;

public class SongServiceImpl implements SongService {

    private SongRepository songRepo;
    private AlbumService albumService;
    private ArtistService artistService;
    private Validator...

    public SongServiceImpl(SongRepository songRepo, AlbumService albumService, ArtistService artistService) {
        this.songRepo = songRepo;
        this.albumService = albumService;
        this.artistService = artistService;
    }

    public boolean add(SongRepository songRepo, Song song, String artistName, String albumName) throws NotFoundException {
        if (!validateArtist(song.getArtist())) {
            throw new ArtistNotFoundException("There is no such artist");
        }
        if (!validateAlbum(song.getAlbum())) {
            throw new AlbumNotFoundException();
        }

        List<Artist> foundArtists = artistService.query( , song.getArtist().getName());
        Artist artistFromDB;
        if(foundArtists.size() != 1){
            throw new DuplicatedRecordException("Smth");
        }
        artistFromDB = foundArtists.get(0);
        song.setArtist(artistFromDB);
        //TODO RETRIEVE ALBUM ID;
        return songRepo.insert(song);
    }

    private boolean validateArtist(Artist artist) {

        return true;
    }

    private boolean validateAlbum(Album album) {

        return true;

    }


    public List<Song> queryByName(SongRepository songRepo, String songName) {
        try {
            return songRepo.queryBySongName(songName);
        } catch (SQLException e) {
            System.out.println("Cannot query songs by song name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(SongRepository songRepo, String artistName, String albumName, String songName) {
        return songRepo.delete(artistName, albumName, songName);

    }

}
