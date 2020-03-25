package com.musicLib.serviceTests;

import com.musicLib.entities.Album;
import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlbumServiceUnitTests {

    private AlbumRepository albumRepoMock;
    private RecordValidator recordValidatorMock;
    private SongService songServiceMock;
    private ArtistService artistServiceMock;
    private AlbumService classToTest;
    private Artist artist;
    private Album album;


    @BeforeEach
    public void setUp() {
        albumRepoMock = mock(AlbumRepository.class);
        classToTest = new AlbumServiceImpl(albumRepoMock);
        recordValidatorMock = mock(RecordValidator.class);
        classToTest.setRecordValidator(recordValidatorMock);
        songServiceMock = mock(SongService.class);
        classToTest.setSongService(songServiceMock);
        artistServiceMock = mock(ArtistService.class);
        classToTest.setArtistService(artistServiceMock);
        album = mock(Album.class);
        artist = mock(Artist.class);
        album.setArtist(artist);
    }

    @Test
    public void testAddMethodWorkedAsExpected() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(Arrays.asList(artist, artist));
        when(artist.getId()).thenReturn(52);
        when(album.getArtist()).thenReturn(artist);
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenReturn(true);
        //Tests
        assertTrue(classToTest.add(album));
    }


    //Question: Should recordValidator throw ValidationException and then Service handle it?
    @Test
    public void testAddValidationFailed() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(Arrays.asList(artist, artist));
        when(artist.getId()).thenReturn(52);
        when(album.getArtist()).thenReturn(artist);
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenThrow(ServiceException.class);
        when(albumRepoMock.add(album)).thenReturn(true);
        //Tests
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
    }

    @Test
    public void testAddValidationPassedRepoFailed() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(Arrays.asList(artist, artist));
        when(artist.getId()).thenReturn(52);
        when(album.getArtist()).thenReturn(artist);
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenThrow(SQLException.class);
        //Tests
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
        assertEquals("Issue with adding album to db", e.getMessage());
    }

    @Test
    public void testAddUpdateAlbumFailed() throws ServiceException, SQLException {
        //Defining expected results for AlbumService: updateAlbumWithArtistID(album)
        when(artistServiceMock.getByName(any())).thenReturn(new ArrayList<>());
        //Service method results defining
        when(recordValidatorMock.validateAlbumAddMethod(album)).thenReturn(true);
        when(albumRepoMock.add(album)).thenThrow(SQLException.class);
        //Tests
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
        assertEquals("Unable to update Album with Artist id", e.getMessage());
    }


    @Test
    public void testGet() throws ServiceException, SQLException {
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.add(album));
        assertEquals("Unable to update Album with Artist id", e.getMessage());
    }
}
