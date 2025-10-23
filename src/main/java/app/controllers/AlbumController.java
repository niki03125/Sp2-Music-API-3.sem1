package app.controllers;

import app.config.HibernateConfig;
import app.daos.AlbumDAO;
import app.dtos.AlbumDTO;
import app.entities.Album;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class AlbumController implements IController<Album,Integer>{

    private final AlbumDAO dao;

    public AlbumController(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AlbumDAO.getInstance(emf);
    }

    @Override
    public void getById(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        Album album = dao.getById(id);

        ctx.res().setStatus(200);
        ctx.json(album, AlbumDTO.class);
    }

    @Override
    public void getAll(Context ctx) {
        List<Album> album = dao.getAll();

        ctx.res().setStatus(200);
        ctx.json(album, AlbumDTO.class);
    }

    @Override
    public void create(Context ctx) {

    }

    @Override
    public void update(Context ctx) {

    }

    @Override
    public void delete(Context ctx) {

    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }

    @Override
    public Album validateEntity(Context ctx) {
        return null;
    }
}
