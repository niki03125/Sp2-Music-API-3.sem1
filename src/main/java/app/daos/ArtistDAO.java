package app.daos;

import app.entities.Artist;
import com.sun.jdi.IntegerType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.w3c.dom.Entity;

import java.util.List;

public class ArtistDAO implements IDAO<Artist, Integer>{
    private static EntityManagerFactory emf;
    private static ArtistDAO instance;

    public ArtistDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    public static ArtistDAO getInstance(EntityManagerFactory emf){
        if(instance == null){
            emf = emf;
            instance = new ArtistDAO(emf);
        }
        return instance;
    }

    @Override
    public Artist create(Artist artist) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(artist);
            em.getTransaction().commit();
            return artist;
        }
    }

    @Override
    public List<Artist> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Artist> query = em.createQuery("select a from Artist a", Artist.class);
            return query.getResultList();
        }
    }

    @Override
    public Artist getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Artist.class, id);
        }
    }

    @Override
    public Artist update(Artist artist) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Artist updated = em.merge(artist);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Artist artist = em.find(Artist.class, id);
            if(artist != null) {
                em.getTransaction().begin();
                em.remove(artist);
                em.getTransaction().commit();
                return true;
            }else{
                return false;
            }
        }catch (PersistenceException ex){
            return false;
        }
    }
}
