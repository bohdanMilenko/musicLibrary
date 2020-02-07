package com.musicLib.serviceTests;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.AlbumNotFoundException;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.SQLightRepository.AlbumRepositorySQL;
import com.musicLib.repository.SQLightRepository.ArtistRepositorySQL;
import com.musicLib.repository.SQLightRepository.SongRepositorySQL;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SongServiceImplIntegrationTest {


    SongService songServiceSQL;
    AlbumService albumServiceSQL;
    ArtistService artistServiceSQL;

    private Song validSongWithNoAlbumAndArtist;
    private Song songInvalidAlbumNoArtist;
    private Song songWithValidAlbumNoArtist;
    private Song validSong;

    //mockito, unit tests, mongoRepo, moreassertions in tests
    //mongoIntegrationTests

    @BeforeEach
    void setUp() {

        initializeServices();

        validSongWithNoAlbumAndArtist = new Song();
        validSongWithNoAlbumAndArtist.setName("New Track");
        validSongWithNoAlbumAndArtist.setTrackNumber(15);

        songInvalidAlbumNoArtist = new Song();
        songInvalidAlbumNoArtist.setName("Earthquake");
        songInvalidAlbumNoArtist.setTrackNumber(11);
        Album invalidAlbum = new Album();
        invalidAlbum.setName("IGOR");
        songInvalidAlbumNoArtist.setAlbum(invalidAlbum);


        songWithValidAlbumNoArtist = new Song();
        songWithValidAlbumNoArtist.setName("New Track");
        songWithValidAlbumNoArtist.setTrackNumber(11);
        Album validAlbum = new Album();
        validAlbum.setName("Pulse");
        songWithValidAlbumNoArtist.setAlbum(validAlbum);

        validSong = new Song();
        validSong.setName("New Track1");
        validSong.setTrackNumber(10);
        Artist validArtist = new Artist();
        validArtist.setName("Pink Floyd");
        validSong.setAlbum(validAlbum);
        validSong.setArtist(validArtist);

    }

    //LIMITATIONS OF DELETE METHOD: UNABLE TO DELETE SONGS MULTIPLE SONGS WITH THE SAME NUMBER, SHOULD I CREATE A NEW METHOD
    //THAT DELETES SONG FROM SPECIFIC ALBUM ?

    @Test
    void addSongNullCheck() {
        assertThrows(ServiceException.class, () -> songServiceSQL.add(null));
    }

    @Test
    @DisplayName("Adding song with album that is not in DB")
    @Description("Service exception must be thrown as song cannot be added to album that does not exist in DB")
    void addSongInvalidAlbumNoArtist(){
        assertThrows(ServiceException.class, () -> songServiceSQL.add(songInvalidAlbumNoArtist));
    }

    @Test
    @DisplayName("Adding song without Album embedded")
    @Description("To add song to DB, the Song object MUST contain Album, if it is not so, AlbumNotFoundException is thrown")
    void addSongNoAlbumNoArtist() {
        assertThrows(AlbumNotFoundException.class, () -> songServiceSQL.add(validSongWithNoAlbumAndArtist));
    }

    @Test
    void addSongValidAlbumNoArtist() {
        assertThrows(ServiceException.class, () -> songServiceSQL.add(songWithValidAlbumNoArtist));
    }

    @Test
    @Description("Please change the name of validSong so you are able to test new name that was not tested and added before")
    void addSongWithValidAlbumAndArtist() throws ServiceException {
        assertTrue(songServiceSQL.add(validSong));
    }

    @Test
    void deleteSongNullCheck(){
        assertThrows(ServiceException.class,() -> songServiceSQL.delete(null));
    }

    @Test
    void deleteSongValidSongNoArtistNoAlbum(){
        assertThrows(ServiceException.class,() -> songServiceSQL.delete(validSongWithNoAlbumAndArtist));
    }

    @Test
    void deleteSongValidSongValidAlbumNoArtist(){
        assertThrows(ServiceException.class,() -> songServiceSQL.delete(songWithValidAlbumNoArtist));
    }

    @Test
    void deleteSongValidSongValidAlbumWithArtist() throws ServiceException{
        assertTrue(songServiceSQL.delete(validSong));
        //CHECK IF SONGS ARE DELETED (GET METHOD)
    }

    //mOCK REPO
    private void initializeServices(){
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