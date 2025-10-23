package rest;

import app.entities.Artist;
import app.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    public Map<String, Song> createSongEntities(EntityManagerFactory emf) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Song").executeUpdate();
            Song s1 = new Song(new Artist(), LocalDate.of(2020,3,1),225,"Pop","TestSang1");
            Song s2 = new Song(new Artist(), LocalDate.of(2020,2,9),125,"Rock","TestSang2");
            Song s3 = new Song(new Artist(), LocalDate.of(2020,5,10),200,"Country","TestSang2");
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.getTransaction().commit();
            Map<String, Song> map = new HashMap<>();
            return Map.of("Song1", s1
                    , "Song2", s2
                    , "Song3", s3
            );
        }
    }
}
