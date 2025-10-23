package app.routes;

import app.controllers.ArtistController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class ArtistRoutes {
    private ArtistController artistController = new ArtistController();

    public EndpointGroup getArtistRoutes(){
        return () -> {
            get("/", artistController::getAll);
            get("/{id}", artistController::getById);
            post("/", artistController::create);
            put("/{id}", artistController::update);
            delete("/{id}", artistController::delete);
        };
    }
}
