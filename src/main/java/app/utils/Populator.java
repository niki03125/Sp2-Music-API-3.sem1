package app.utils;

import app.config.HibernateConfig;
import app.entities.Album;
import app.entities.Artist;
import app.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public class Populator {
    public void createSongEntities(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Song").executeUpdate();
            Song s1 = new Song(new Artist(),LocalDate.of(2020,3,13),301,"Pop","TestSang1");
            Song s2 = new Song(new Artist(),LocalDate.of(2021,6,23),150,"Rock","TestSang2");
            Song s3 = new Song(new Artist(),LocalDate.of(2023,12,9),225,"Rap","TestSang3");
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.getTransaction().commit();

            System.out.println("Songs in DB: ");
            em.createQuery("SELECT s FROM Song s", Song.class).getResultList().forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        Populator populator = new Populator();

        populator.createSongEntities(HibernateConfig.getEntityManagerFactory());
    }
}
