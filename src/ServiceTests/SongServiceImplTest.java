package ServiceTests;

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

class SongServiceImplTest {

    SongService songServiceSQL;
    AlbumService albumServiceSQL;
    ArtistService artistServiceSQL;

    private Song songWithNoAlbumAndArtist;
    private Song songWithNoArtistAndInvalidAlbum;
    private Song songWithAlbumAndArtist;
    private Song songWithNoArtistAndValidAlbum;
    private  Song validSong;

    @BeforeEach
    void initServices() {
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

        songWithNoAlbumAndArtist = new Song();
        songWithNoAlbumAndArtist.setName("New Magic Wand");
        songWithNoAlbumAndArtist.setTrackNumber(15);

        songWithNoArtistAndInvalidAlbum = new Song();
        songWithNoArtistAndInvalidAlbum.setName("Earthquake");
        songWithNoArtistAndInvalidAlbum.setTrackNumber(11);
        Album invalidAlbum = new Album();
        invalidAlbum.setName("IGOR");
        songWithNoArtistAndInvalidAlbum.setAlbum(invalidAlbum);


        songWithNoArtistAndValidAlbum = new Song();
        songWithNoArtistAndValidAlbum.setName("Boy is a gun");
        songWithNoArtistAndValidAlbum.setTrackNumber(11);
        Album validAlbum = new Album();
        validAlbum.setName("Pulse");
        songWithNoArtistAndValidAlbum.setAlbum(validAlbum);

        validSong = new Song();
        validSong.setName("New Track");
        Artist validArtist = new Artist();
        validArtist.setName("Pink Floyd");
        validSong.setAlbum(validAlbum);
        validSong.setArtist(validArtist);

    }

    @Test
    void addNullCheck(){
        assertThrows(ServiceException.class, ()-> songServiceSQL.add(null));
    }

    @Test
    @DisplayName("Adding song with album that is not in DB")
    @Description("Service exception must be thrown as song cannot be added to album that does not exist in DB")
    void addSongWithNoAlbumInDB() throws ServiceException {
        assertThrows(ServiceException.class,() -> songServiceSQL.add(songWithNoArtistAndInvalidAlbum));
    }

    @Test
    @DisplayName("Adding song without Album embedded")
    @Description("To add song to DB, the Song object MUST contain Album, if it is not so, AlbumNotFoundException is thrown")
    void addSongWithoutAlbumAndArtist(){
        assertThrows(AlbumNotFoundException.class,() -> songServiceSQL.add(songWithNoAlbumAndArtist));
    }

    @Test
    void addSongWithoutArtistAndValidAlbum() throws ServiceException {
        assertThrows(ServiceException.class,()->songServiceSQL.add(songWithNoArtistAndValidAlbum));
    }


    //TODO ISSUES: TRACK IS ADDED WITH ID OF THE ARTIST NOT ALBUM AND IT IS POSSIBLE TO ADD DUPLICATED SONG!
    @Test
    void addSongWithValidAlbumAndArtist() throws ServiceException{
        System.out.println(validSong.toString());
        assertTrue(songServiceSQL.add(validSong));
    }
}