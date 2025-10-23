package app.controllers;

import app.config.HibernateConfig;
import app.daos.AlbumDAO;
import app.daos.ArtistDAO;
import app.daos.SongDAO;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import app.exceptions.ApiException;
import app.services.AlbumService;
import app.services.ArtistService;
import app.services.SongService;
import io.javalin.http.Context;

import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ArtistController implements IController<Artist,Integer>{
    private final ArtistService artistService;
    private final ArtistDAO artistDao;
    private final AlbumDAO albumDAO;
    private final SongDAO songDAO;

    private final AlbumService albumService;
    private final SongService songService;


    public ArtistController(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        // Dao layer
        this.artistDao = new ArtistDAO(emf);
        this.albumDAO = new AlbumDAO(emf);
        this.songDAO = new SongDAO(emf);

        // Service layer
        this.albumService = new AlbumService(albumDAO);
        this.songService = new SongService(songDAO);

        this.artistService = new ArtistService(artistDao, albumService, songService);
    }

    @Override
    public void getById(Context ctx) {
        // Get id from url and check valid/invalid
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey,"Not valid artist id")
                .get();

        ArtistDTO artistDTO = artistService.getById(id);

        if(artistDTO == null){
            throw new ApiException(404,"The artist was not found"); // 404 not found
        }
        ctx.status(200).json(artistDTO); // 200 ok
    }

    @Override
    public void getAll(Context ctx) {
        // Get all using the service
        List<ArtistDTO> artistDTOS = artistService.getAll();
        ctx.status(200).json(artistDTOS);
    }

    @Override
    public void create(Context ctx) {
        // Read request body as artistDTO
        ArtistDTO artistDTO = ctx.bodyAsClass(ArtistDTO.class);

        // Make artist using service
        ArtistDTO dto = artistService.create(artistDTO);

        ctx.status(201).json(dto); // 201 created
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id",Integer.class)
                .check(this::validatePrimaryKey,"Not a valid id")
                .get();

        // Read body as artistDTO
        ArtistDTO artistDTO = ctx.bodyAsClass(ArtistDTO.class);

        // Update by service
        ArtistDTO updated = artistService.update(id, artistDTO);

        if(updated == null){
            throw new ApiException(404,"Artist was not found");
        }
        ctx.status(200).json(updated);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id",Integer.class)
                .check(this::validatePrimaryKey,"Not a valid id")
                .get();

        boolean deleted = artistService.delete(id);

        if(!deleted) {
            throw new ApiException(404, "Artist was not found");
        }
        // success
        ctx.status(204); // No content
    }

    // does the id exist in the db using dao to figure out
    @Override
    public boolean validatePrimaryKey(Integer id) {
        return artistDao.validatePrimaryKey(id);
    }

    @Override
    public Artist validateEntity(Context ctx) {
        return null;
    }
}
