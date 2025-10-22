package app.utils;

import app.config.HibernateConfig;
import app.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public class Populator {
    public void createSongEntities(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Song").executeUpdate();
            Song s1 = new Song("TestSang", "Rap", 225, LocalDate.of(2020, 3, 20));
            Song s2 = new Song("TestSang2", "Country", 201, LocalDate.of(2022, 12, 12));
            Song s3 = new Song("TestSang3", "Rock", 315, LocalDate.of(2023, 9, 13));
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
