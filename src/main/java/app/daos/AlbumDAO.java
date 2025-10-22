package app.daos;

import app.entities.Album;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AlbumDAO implements IDAO<Album, Integer>{
    private static EntityManagerFactory emf;
    private static AlbumDAO instance;

    public AlbumDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    public static AlbumDAO getInstance(EntityManagerFactory _emf){
        if(instance == null){
            emf =_emf;
            instance = new AlbumDAO(emf);
        }
        return instance;
    }

    @Override
    public Album create(Album album) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(album);
            em.getTransaction().commit();
        }
        return album;
    }

    @Override
    public List<Album> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a", Album.class);
            return query.getResultList();
        }
    }

    @Override
    public Album getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Album.class, id);
        }
    }

    @Override
    public Album update(Album album) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Album updatedAlbum = em.merge(album);
            em.getTransaction().commit();
            return updatedAlbum;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Album A = em.find(Album.class, id);
            if(A != null){
                em.getTransaction().begin();
                em.remove(A);
                em.getTransaction().commit();
                return true;
            } else {
                return false;
            }
        }catch (PersistenceException ex){
            return false;
        }
    }
}
