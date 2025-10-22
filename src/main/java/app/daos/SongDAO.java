package app.daos;

import app.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;


public class SongDAO implements IDAO<Song,Integer> {
    private static EntityManagerFactory emf;
    private static SongDAO instance;

    public SongDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    public static SongDAO getInstance(EntityManagerFactory _emf){
        if(instance == null){
            emf =_emf;
            instance = new SongDAO(emf);
        }
        return instance;
    }


    @Override
    public Song create(Song song) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(song);
            em.getTransaction().commit();
            return song;
        }
    }

    @Override
    public List<Song> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Song> query = em.createQuery("SELECT s FROM Song s", Song.class);
            return query.getResultList();
        }
    }

    @Override
    public Song getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Song.class, id);
        }
    }

    @Override
    public Song update(Song song) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Song updatedSong = em.merge(song);
            em.getTransaction().commit();
            return updatedSong;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Song songToDelete = em.find(Song.class, id);
            if(songToDelete != null){
                em.getTransaction().begin();
                em.remove(songToDelete);
                em.getTransaction().commit();
                return true;
            }else {
                return false;
            }
        }
    }
}
