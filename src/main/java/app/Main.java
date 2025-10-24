package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.AlbumDAO;
import app.daos.ArtistDAO;
import app.daos.SongDAO;

import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.dtos.SongDTO;
import app.entities.Album;
import app.entities.Artist;
import app.entities.Song;
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
            ///   ///////////// Peter(ARTIST)//////////////////////

            long artistId = 1;
            Artist savedArtist1 = artistService.saveArtistFromDeezer(artistId);
            System.out.println("Saved artist: " + savedArtist1.getName());

            long artistId2 = 2;
            Artist savedArtist2 = artistService.saveArtistFromDeezer(artistId2);
            System.out.println("Saved artist: " + savedArtist2.getName());

            long artistId3 = 3;
            Artist savedArtist3 = artistService.saveArtistFromDeezer(artistId3);
            System.out.println("Saved artist: " + savedArtist3.getName());


            /// ///////////////// Niki(SONGS)///////////////////////////

            //empoter en sang, bind til artist, gem i db
            int deezerTrackId1 = 3135556;
            Integer localAlbumId1 = null;
            SongDTO importedSong1 = songService.importFromDeezerTrack(deezerTrackId1, savedArtist1.getId(), localAlbumId1);
            System.out.println("Imported Song" + importedSong1);

            int deezerTrackId2 = 3135602;
            Integer localAlbumId2 = null;
            SongDTO importedSong2 = songService.importFromDeezerTrack(deezerTrackId2, savedArtist2.getId(),localAlbumId2);
            System.out.println("Imported Song" + importedSong2);

            //Læs kun fra db
            List<SongDTO> all = songService.getAll();
            System.out.println("Songs in our db " + all.size());
            all.forEach(System.out::println);

            SongDTO byId = songService.getById(importedSong1.getId());
            System.out.println("Song fetched by id: " + byId);

            /// ///////////////////// Janhcke (ALBUM)////////////////////

            int albumId = 302127;
            Album importedAlbum = albumService.saveAlbumFromDeezer(albumId);
            System.out.println("Imported " + importedAlbum);

        } catch (ApiException e) {
            System.err.println("Fejl: " + e.getMessage());
        }
    }

}