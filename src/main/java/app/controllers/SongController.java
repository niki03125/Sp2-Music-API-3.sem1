package app.controllers;

import app.config.HibernateConfig;
import app.daos.SongDAO;
import app.dtos.SongDTO;
import app.entities.Song;
import app.exceptions.NotFoundException;
import app.services.SongService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class SongController implements IController<Song,Integer>{
    private SongDAO dao;

    public SongController(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = SongDAO.getInstance(emf);
    }

    @Override
    public void getById(Context ctx) {
    int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
    Song song = dao.getById(id);
    if(song==null){
        ctx.status(404).result("not found");
        return;
    }
    ctx.status(200).json(SongService.toDTO(song));
    }

    @Override
    public void getAll(Context ctx) {

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
    public Song validateEntity(Context ctx) {
        return null;
    }
}
