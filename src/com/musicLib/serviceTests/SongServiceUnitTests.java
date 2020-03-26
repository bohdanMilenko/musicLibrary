package com.musicLib.serviceTests;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.entities.Song;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SongServiceUnitTests {


    private SongRepository songRepoMock;
    private RecordValidator recordValidatorMock;
    private AlbumService albumServiceMock;
    private SongService classToTest;
    private Artist artist;
    private Album album;
    private Song song;

    @BeforeEach
    public void setUp() {
        songRepoMock = mock(SongRepository.class);
        classToTest = new SongServiceImpl (songRepoMock);
        recordValidatorMock = mock(RecordValidator.class);
        classToTest.setRecordValidator(recordValidatorMock);
        albumServiceMock = mock(AlbumService.class);
        classToTest.setAlbumService(albumServiceMock);

        album = mock(Album.class);
        artist = mock(Artist.class);
        album.setArtist(artist);
        song = mock(Song.class);
        song.setAlbum(album);
        song.setArtist(artist);

    }

    //This test doesn't go inside of the method,  returns default false
    @Test
    void testAddWorkAsExpected() throws ServiceException, SQLException {
        when(recordValidatorMock.validateSongAddMethod(any(Song.class))).thenReturn(true);
        when(songRepoMock.add(song)).thenReturn(true);
        assertTrue(classToTest.add(song));
    }

}
