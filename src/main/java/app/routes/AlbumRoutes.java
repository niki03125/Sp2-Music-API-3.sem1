package app.routes;

import app.controllers.AlbumController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AlbumRoutes {
    private final AlbumController albumController =  new AlbumController();

    public EndpointGroup getAlbumRoutes(){
        return () -> {
            get("/", albumController::getAll);
            get("/{id}", albumController::getById);
            post("/", albumController::create);
            put("/{id}", albumController::update);
            delete("/{id}", albumController::delete);
        };
    }


}
