package app.routes;

import app.controllers.AlbumController;
import app.controllers.SongController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class SongRoutes {
    private final SongController songController =  new SongController();

    public EndpointGroup getSongRoutes(){
        return () -> {
            post("/api/v1/songs/import/deezer/:trackId", songController::impo);
            get("/", songController::getAll);
            get("/{id}", songController::getById);
            post("/", songController::create);
            put("/{id}", songController::update);
            delete("/{id}", songController::delete);
        };
    }
}
