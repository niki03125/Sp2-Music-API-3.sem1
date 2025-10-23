package app.controllers;

import app.config.HibernateConfig;
import app.daos.SongDAO;
import app.dtos.SongDTO;
import app.entities.Song;
import app.exceptions.NotFoundException;
import app.services.SongService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SongController implements IController<SongDTO,Integer>{
    private final SongService songService;

    public SongController(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.songService = new SongService(SongDAO.getInstance(emf));
    }

    //GET /api/songs/:id
    @Override
    public void getById(Context ctx) {

    int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();//Læs Id fra url, og tjek at det er gyldigt
    SongDTO songDTO = songService.getById(id); // brug songService til at hente sang som dto

    if(songDTO==null){
        ctx.status(404).result("Song not found");
        return;
    }
    ctx.status(200).json(songDTO);//retuner sang som json
    }

    //GET /api/songs
    @Override
    public void getAll(Context ctx) {
        List<SongDTO> songs = songService.getAll(); //henter og mapper til DTOer
        ctx.status(200).json(songs); //retuner som json
    }

    //POST /api/songs
    @Override
    public void create(Context ctx) {
        SongDTO songDTO = validateEntity(ctx); //valider + får DTO tilbage
        SongDTO created = songService.create(songDTO); // Send DTO til SongService: DTO -> entity -> save ->DTO
        ctx.status(201).json(created);
    }

    //PUT /api/songs/:id
    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();//Læs Id fra url, og tjek at det er gyldigt
        SongDTO songDTO = validateEntity(ctx); // læs request body  og validere(Json-> Dto + tjek)
        SongDTO updated = songService.update(id,songDTO); // send til service , tjek om sang findes og opdater

        if (updated == null){
            ctx.status(404).result("Song not found");
            return;
        }
        ctx.status(200).json(updated);//retuner den opaterede sang
    }

    //DELETE /api/songs/:id
    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();//Læs Id fra url, og tjek at det er gyldigt
        boolean deleted = songService.delete(id); // kalder songservice, sletter i bd
       ctx.status(deleted ? 204 :404);// hvis  du får 204, har det være successful sletning
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return id != null && id> 0;
    }

    @Override
    public SongDTO validateEntity(Context ctx) {
         return ctx.bodyValidator(SongDTO.class)//validering
                .check(s -> s.getTitle() != null && !s.getTitle().isBlank(), "Title is requered")// tjekker om der en title og den ikke er tom ligesom = " "
                .check(s -> s.getDuration() > 0, "the song must be longer than 0 sek.") //tjekker at sangen er længere end 0 sek.
                .check(s -> s.getArtistId() != null && s.getArtistId() > 0,"ArtisID is requiered")//tjekker om der er en artist og id er str end 0
                .check(s -> s.getReleaseDate() != null && !s.getReleaseDate().isBlank(), "releaseDate is required")//realeaseDate skal være sat
                .get();
    }
}
