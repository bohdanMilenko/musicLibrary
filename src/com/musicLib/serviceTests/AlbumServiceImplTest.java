package com.musicLib.serviceTests;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.SQLightRepository.AlbumRepositorySQL;
import com.musicLib.repository.SQLightRepository.ArtistRepositorySQL;
import com.musicLib.repository.SQLightRepository.SongRepositorySQL;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlbumServiceImplTest {

    SongService songServiceSQL;
    AlbumService albumServiceSQL;
    ArtistService artistServiceSQL;

    private Album invalidAlbumValidArtist;
    private Album validPresentAlbum;
    private Album validNotPresentAlbum;
    private Album newAlbumNoArtist;
    private Album newAlbumArtistNotPresentInDb;

    @BeforeEach
    void setUp() {

        initializeServices();

        Artist validArtist = new Artist();
        validArtist.setName("Pink Floyd");

        Artist invalidArtist = new Artist();
        invalidArtist.setName("Invalid Artist");

        validPresentAlbum = new Album();
        validPresentAlbum.setArtist(validArtist);
        validPresentAlbum.setName("Pulse");

        validNotPresentAlbum = new Album();
        validNotPresentAlbum.setArtist(validArtist);
        validNotPresentAlbum.setName("New Album");

        newAlbumNoArtist = new Album();
        newAlbumNoArtist.setName("New Album No Artist");

        invalidAlbumValidArtist = new Album();
        invalidAlbumValidArtist.setArtist(validArtist);
        invalidAlbumValidArtist.setName("Purple Haze");

        newAlbumArtistNotPresentInDb = new Album();
        newAlbumArtistNotPresentInDb.setName("New Album - Artist not present in db");
        newAlbumArtistNotPresentInDb.setArtist(invalidArtist);

    }

    @Test
    void addNull() {
        assertThrows(ServiceException.class, () ->albumServiceSQL.add(null));
    }

    @Test
    void addInvalidAlbumValidArtist() {
        assertThrows(ServiceException.class, () ->albumServiceSQL.add(invalidAlbumValidArtist));
    }

    @Test
    void addNewAlbumNoArtist() {
        assertThrows(ServiceException.class, () ->albumServiceSQL.add(newAlbumNoArtist));
    }

    @Test
    void addNewAlbumArtistNotPresentInDB() {
        assertThrows(ServiceException.class, () ->albumServiceSQL.add(newAlbumArtistNotPresentInDb));
    }

    @Test
    void addValidAlbumValidArtist() throws  ServiceException {
        assertTrue(albumServiceSQL.add(validNotPresentAlbum));
    }

    private void initializeServices() {
        ArtistRepository artistRepositorySQLite = new ArtistRepositorySQL();
        AlbumRepository albumRepositorySQLite = new AlbumRepositorySQL();
        SongRepository songRepositorySQLite = new SongRepositorySQL();

        songServiceSQL = new SongServiceImpl(songRepositorySQLite);
        albumServiceSQL = new AlbumServiceImpl(albumRepositorySQLite);
        artistServiceSQL = new ArtistServiceImpl(artistRepositorySQLite);

        RecordValidator recordValidator = new RecordValidator(artistServiceSQL, albumServiceSQL, songServiceSQL);

        songServiceSQL.setAlbumService(albumServiceSQL);
        songServiceSQL.setRecordValidator(recordValidator);

        artistServiceSQL.setAlbumService(albumServiceSQL);
        artistServiceSQL.setRecordValidator(recordValidator);

        albumServiceSQL.setSongService(songServiceSQL);
        albumServiceSQL.setArtistService(artistServiceSQL);
        albumServiceSQL.setRecordValidator(recordValidator);
    }
}