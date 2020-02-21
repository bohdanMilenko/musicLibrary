package com.musicLib.serviceTests;

import com.musicLib.entities.Artist;
import com.musicLib.exceptions.ServiceException;
import com.musicLib.repository.AlbumRepository;
import com.musicLib.repository.ArtistRepository;
import com.musicLib.repository.MongoDBRepisotory.AlbumRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.ArtistRepositoryMongo;
import com.musicLib.repository.MongoDBRepisotory.SongRepositoryMongo;
import com.musicLib.repository.SongRepository;
import com.musicLib.services.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.hamcrest.SelfDescribing;

import org.mockito.plugins.MockMaker;

@ExtendWith(MockitoExtention.class)
public class ArtistServiceImplMockTests {

    SongService songServiceMongo;
    AlbumService albumServiceMongo;
    ArtistService artistServiceMongo;

    @Mock
    private ArtistRepository artistRepoMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testAdd() throws ServiceException {
        ArtistService artistService = new ArtistServiceImpl(artistRepoMock);
        Artist artist = new Artist();
        artist.setName("Poppy");
        assertTrue(artistService.add(artist));
    }


    private ArtistService initializeServices() {
        ArtistRepository artistRepositoryMongo = new ArtistRepositoryMongo();
        AlbumRepository albumRepositoryMongo = new AlbumRepositoryMongo();
        SongRepository songRepositoryMongo = new SongRepositoryMongo();

        songServiceMongo = new SongServiceImpl(songRepositoryMongo);
        albumServiceMongo = new AlbumServiceImpl(albumRepositoryMongo);
        artistServiceMongo = new ArtistServiceImpl(artistRepositoryMongo);

        RecordValidator recordValidator = new RecordValidator(artistServiceMongo, albumServiceMongo, songServiceMongo);

        songServiceMongo.setAlbumService(albumServiceMongo);
        songServiceMongo.setRecordValidator(recordValidator);

        artistServiceMongo.setAlbumService(albumServiceMongo);
        artistServiceMongo.setRecordValidator(recordValidator);

        albumServiceMongo.setSongService(songServiceMongo);
        albumServiceMongo.setArtistService(artistServiceMongo);
        albumServiceMongo.setRecordValidator(recordValidator);

        return artistServiceMongo;
    }





}
