package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.AlbumDAO;
import app.daos.ArtistDAO;
import app.daos.SongDAO;
import app.dtos.SongDTO;
import app.entities.Artist;
import app.exceptions.ApiException;
import app.services.AlbumService;
import app.services.ArtistService;
import app.services.DeezerService;
import app.services.SongService;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(7007);
        DeezerService deezerService = new DeezerService();
        String json = deezerService.getInfoFromUrl("https://api.deezer.com/track/3135556");
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        AlbumDAO albumDAO = AlbumDAO.getInstance(emf); // SÃ¸rg for AlbumDAO har singleton
        SongDAO songDAO = SongDAO.getInstance(emf);
        ArtistDAO artistDAO = ArtistDAO.getInstance(emf);
        AlbumService albumService = new AlbumService(albumDAO);
        SongService songService = new SongService(songDAO);

        ArtistService artistService = new ArtistService(artistDAO, albumService, songService);

        try {
            long artistId = 10; // fx Daft Punk
            Artist savedArtist = artistService.saveArtistFromDeezer(artistId);
            System.out.println("Saved artist: " + savedArtist.getName());

            //empoter en sang, bind til artist, gem i db
            int deezerTrackId1 = 3135556;
            Integer localAlbumId1 = null;
            SongDTO importedSong1 = songService.importFromDeezerTrack(deezerTrackId1, savedArtist.getId(), localAlbumId1);
            System.out.println("Imported Song" + importedSong1);

            int deezerTrackId2 = 3135602;
            Integer localAlbumId2 = null;
            SongDTO importedSong2 = songService.importFromDeezerTrack(deezerTrackId2, savedArtist.getId(),localAlbumId2);
            System.out.println("Imported Song" + importedSong2);

            //Læs kun fra db
            List<SongDTO> all = songService.getAll();
            System.out.println("Songs in our db " + all.size());
            all.forEach(System.out::println);



        } catch (ApiException e) {
            System.err.println("Fejl: " + e.getMessage());
        }
        System.out.println(json);

    }

}