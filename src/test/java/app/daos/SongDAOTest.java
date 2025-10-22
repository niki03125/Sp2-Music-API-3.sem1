package app.daos;

import app.config.HibernateConfig;
import app.entities.Song;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

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

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
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