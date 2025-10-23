package app.daos;

import app.config.HibernateConfig;
import app.entities.Artist;
import app.entities.Song;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import rest.TestUtils;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SongDAOTest {
    private static EntityManagerFactory emf;
    private static SongDAO songDAO;

    Song s1, s2, s3;

    @BeforeAll
    static void setupAll() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactory();
        songDAO = new SongDAO(emf);
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTest(false);
    }

    @BeforeEach
    void setUp() {
        TestUtils testUtils = new TestUtils();
        Map<String, Song> populated = testUtils.createSongEntities(emf);
        s1 = (Song) populated.get("1");
        s2 = (Song) populated.get("2");
        s3 = (Song) populated.get("3");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        Song newSong = new Song(new Artist(), LocalDate.of(2020,7,12), 225,"Pop,", "Skyfall");
        Song created = songDAO.create(newSong);
        assert created.getSongId() != null;
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}