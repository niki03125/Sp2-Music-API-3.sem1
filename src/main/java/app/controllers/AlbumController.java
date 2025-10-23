package app.controllers;

import app.config.HibernateConfig;
import app.daos.AlbumDAO;
import app.dtos.AlbumDTO;
import app.entities.Album;
import app.services.AlbumService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;

public class AlbumController implements IController<Album,Integer>{

    private final AlbumDAO dao;
    private final AlbumService service;

    public AlbumController(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AlbumDAO.getInstance(emf);
        this.service = new AlbumService(dao);
    }

    @Override
    public void getById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Album album = dao.getById(id);
        AlbumDTO dto = service.toAlbumDTO(album);
        if(dto == null){
            ctx.status(404).json(Map.of("message", "Album not found"));
        }else{
            ctx.json(dto);
        }
    }

    @Override
    public void getAll(Context ctx) {
        List<Album> albums = dao.getAll();
        List<AlbumDTO> albumDTOS = service.toAlbumDTOList(albums);
        ctx.json(album, AlbumDTO.class);
    }

    @Override
    public void create(Context ctx) {
        AlbumDTO dto = ctx.bodyAsClass(AlbumDTO.class);
        Album album = service.toAlbumEntity(dto);

        Album createdAlbum = dao.create(album);
        ctx.status(201).json(service.toAlbumDTO(createdAlbum));
    }

    @Override
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        AlbumDTO dto = ctx.bodyAsClass(AlbumDTO.class);

        Album album = service.toAlbumEntity(dto);
        album.setAlbumId(id);

        Album updatedAlbum = dao.update(album);
        ctx.json(service.toAlbumDTO(updatedAlbum));

    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Album album = dao.getById(id);
        AlbumDTO dto = service.toAlbumDTO(album);

        if(dto == null){
            ctx.status(404).json(Map.of("message", "Album not found"));
        }else{
            dao.delete(id);
            ctx.status(204);
        }
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
