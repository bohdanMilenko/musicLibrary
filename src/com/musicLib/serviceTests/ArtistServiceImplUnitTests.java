package com.musicLib.serviceTests;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.*;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.SongRepositoryMongo;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArtistServiceImplUnitTests {


    //ArtistServiceImp - SUT System under Test
    //ArtistRepo - is a dependency
    SongService songServiceMongo;
    AlbumService albumService;
    ArtistRepository artistRepositoryStub = new ArtistRepositoryMongoTestStub();
    ArtistService artistServiceMongoStubBased = new ArtistServiceImpl(artistRepositoryStub);


    private ArtistRepository artistRepositoryMock;
    private RecordValidator recordValidator;
    private ArtistService classToTest;
    private Artist artist;

    @BeforeEach
    public void setUp() {
        artistRepositoryMock = mock(ArtistRepository.class);
        recordValidator = mock(RecordValidator.class);
        classToTest = spy(new ArtistServiceImpl(artistRepositoryMock));
        classToTest.setRecordValidator(recordValidator);
        albumService = mock(AlbumService.class);
        classToTest.setAlbumService(albumService);


        artist = new Artist();
        artist.setName("Bob Dylan");
    }

    @Test
    public void testAddValidationPassedObjectAddedToDB() throws ServiceException, SQLException {
        when(recordValidator.validateArtistAddMethod(artist)).thenReturn(true);
        when(artistRepositoryMock.add(artist)).thenReturn(true);
        assertTrue(classToTest.add(artist));
    }


    @Test
    public void testAddValidationNotPassed() throws ServiceException {
        when(recordValidator.validateArtistAddMethod(artist)).thenThrow(ServiceException.class);
        ServiceException exception = assertThrows(ServiceException.class, () -> classToTest.add(artist));
        assertNull(exception.getMessage());
    }


    @Test
    public void testAddValidationPassedObjectNotAddedToDB() throws ServiceException {
        when(recordValidator.validateArtistAddMethod(artist)).thenReturn(true);
        when(classToTest.add(artist)).thenThrow(QueryException.class);
        assertThrows(ServiceException.class, () -> classToTest.add(artist));
    }


    @Test
    public void testAddSQLExceptionThrown() throws ServiceException, SQLException {
        when(recordValidator.validateArtistAddMethod(artist)).thenReturn(true);
        when(artistRepositoryMock.add(artist)).thenThrow(SQLException.class);
        ServiceException exception = assertThrows(ServiceException.class, () -> classToTest.add(artist));
        assertEquals("Cannot insert artist to db", exception.getMessage());
        assertEquals(SQLException.class, exception.getCause().getClass());
    }

    @Test
    public void testGetAllMethodUsingStub() throws ServiceException {
        List<Artist> returnedArtists = artistServiceMongoStubBased.getAll();
        assertEquals(2, returnedArtists.size());
    }

    @Test
    public void testGetAllUsingMock() throws ServiceException {
        when(classToTest.getAll()).thenReturn(Arrays.asList(new Artist(), new Artist()));
        List<Artist> returnedArtists = classToTest.getAll();
        assertEquals(2, returnedArtists.size());
        assertEquals(returnedArtists.get(0).getClass(), Artist.class);
    }

    @Test
    public void testGetAllValidationPassedRepoFailedSQL() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(classToTest.getAll()).thenThrow(SQLException.class);
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.getAll());
        assertEquals("Issue with getting all Artists" , e.getMessage());
    }

    @Test
    public void testGetByNameValidationPassedRepoWorkedAsExpected() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(classToTest.getByName(artist)).thenReturn(Arrays.asList(new Artist(), new Artist()));
        List<Artist> returnedArtists = classToTest.getByName(artist);
        assertEquals(2, returnedArtists.size());
        assertEquals(returnedArtists.get(0).getClass(), Artist.class);
    }

    @Test
    public void testGetByNameValidationNotPassed() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenCallRealMethod();
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.getByName(null));
        assertEquals("Passed object is null" , e.getMessage());
    }

    @Test
    public void testGetByNameValidationPassedRepoFailed() throws ServiceException {
        when(recordValidator.validateIfNotNull(any())).thenReturn(true);
        when(classToTest.getByName(artist)).thenThrow(SQLException.class);
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.getByName(artist));
        assertEquals("Failed to get Artist by Name", e.getMessage());
        assertEquals(SQLException.class, e.getCause().getClass());
    }


    //TODO NOT FINISHED
    @Test
    public void testDeleteValidationPassedRepoWorkedAsExpectedHasDependantAlbums() throws ServiceException, SQLException {
        when(recordValidator.validateArtistDeleteMethod(artist)).thenReturn(true);
        when(classToTest.updateArtistID(artist)).thenReturn(artist);
        when(recordValidator.hasDependantAlbums(artist)).thenReturn(true);
        when(albumService.deleteAlbumsForArtist(artist)).thenReturn(true);
        assertTrue(classToTest.delete(artist));
    }

    @Test
    public void testDeleteValidationFailed() throws ServiceException {
        when(recordValidator.validateArtistDeleteMethod(artist)).thenThrow(ArtistNotFoundException.class);
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.delete(artist));
        assertEquals("Unable to delete artist: " + artist.getName(), e.getMessage());
    }

    //No message is returned, is it supposed to be so?
    @Test
    public void testDeleteValidationFailed2() throws ServiceException {
        when(recordValidator.validateArtistDeleteMethod(artist)).thenThrow(ServiceException.class);
        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.delete(artist));
        assertEquals("Passed object is null", e.getMessage());
    }


    //I am trying here to test classToTest, but not mocked objects. Am I supposed to test only dependencies?
//    @Test
//    public void testDeleteValidationPassedUpdateFailed() throws ServiceException {
//        when(recordValidator.validateArtistDeleteMethod(artist)).thenReturn(true);
//        doThrow(ServiceException.class).when(classToTest.updateArtistID(artist));
//        //when(classToTest.updateArtistID(artist)).thenThrow(ServiceException.class);
//        ServiceException e = assertThrows(ServiceException.class, () -> classToTest.delete(artist));
//    }


    @Test
    public void testDeleteValidationPassedRepoFailed() throws ServiceException, SQLException {
        when(recordValidator.validateArtistDeleteMethod(artist)).thenReturn(true);
        List<Artist> returnedArtists = new ArrayList<>();
        returnedArtists.add(artist);
        when(recordValidator.hasDependantAlbums(any(Artist.class))).thenReturn(true);
        doReturn(returnedArtists).when(classToTest).getByName(artist);
        //when(classToTest.getByName(artist)).thenReturn(returnedArtists);
        when(classToTest.updateArtistID(artist)).thenReturn(artist);
        when(albumService.deleteAlbumsForArtist(artist)).thenThrow(ArtistNotFoundException.class);
        ServiceException e = assertThrows(ArtistNotFoundException.class, () -> classToTest.delete(artist));
        assertEquals("Such artist does not exist", e.getMessage());
    }






    private ArtistService initializeServices() {
        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        AlbumRepository albumRepositoryMongo = new AlbumRepositoryMongo();
        SongRepository songRepositoryMongo = new SongRepositoryMongo();

        songServiceMongo = new SongServiceImpl(songRepositoryMongo);
        albumService = new AlbumServiceImpl(albumRepositoryMongo);
        artistServiceMongoStubBased = new ArtistServiceImpl(artistRepositoryMongo);

        RecordValidator recordValidator = new RecordValidator(artistServiceMongoStubBased, albumService, songServiceMongo);

        songServiceMongo.setAlbumService(albumService);
        songServiceMongo.setRecordValidator(recordValidator);

        artistServiceMongoStubBased.setAlbumService(albumService);
        artistServiceMongoStubBased.setRecordValidator(recordValidator);

        albumService.setSongService(songServiceMongo);
        albumService.setArtistService(artistServiceMongoStubBased);
        albumService.setRecordValidator(recordValidator);

        return artistServiceMongoStubBased;
    }


}
