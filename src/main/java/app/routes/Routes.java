package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    AlbumRoutes albumRoutes = new AlbumRoutes();
    private ArtistRoutes artistRoutes = new ArtistRoutes();

    public EndpointGroup getRoutes(){
        return () -> {
            get("/", ctx -> ctx.result("Hello from the music api"));
            path("/artist", () -> {
                artistRoutes.getArtistRoutes();
            });
            path("/albums", () -> {
                albumRoutes.getAlbumRoutes();
            });
        };
    }

}
