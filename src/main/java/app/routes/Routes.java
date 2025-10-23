package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    AlbumRoutes albumRoutes = new AlbumRoutes();

    public EndpointGroup getRoutes(){
        return () -> {
            get("/", ctx -> ctx.result("Hello from the music api"));

            path("/albums", () -> {
                albumRoutes.getAlbumRoutes().addEndpoints();
            });
        };
    }

}
