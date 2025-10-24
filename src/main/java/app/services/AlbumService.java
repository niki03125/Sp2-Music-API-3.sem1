package app.services;

import app.daos.AlbumDAO;
import app.daos.ArtistDAO;
import app.dtos.AlbumDTO;
import app.dtos.GenreDTO;
import app.entities.Album;
import app.entities.Artist;
import app.entities.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class AlbumService {
    private final AlbumDAO dao;

    public AlbumService(AlbumDAO dao){
        this.dao = dao;
    }

    public AlbumDTO createAlbum(AlbumDTO dto){
        Album album = toAlbumEntity(dto);
        Album newAlbum = dao.create(album);
        return toAlbumDTO(newAlbum);
    }

    public AlbumDTO getAlbumById(int id){
        Album album = dao.getById(id);
        return (album != null) ? toAlbumDTO(album) : null;
    }

    public List<AlbumDTO> getAllAlbums(){
        List<Album> albums = dao.getAll();
        return albums.stream()
                .map(AlbumService::toAlbumDTO)
                .collect(Collectors.toList());
    }

    public static AlbumDTO toAlbumDTO(Album album){
        return AlbumDTO.builder()
                .albumId(album.getAlbumId())
                .albumTitle(album.getAlbumTitle())
                .genre(album.getGenre() != null ? GenreDTO.builder()
                        .data(List.of(GenreDTO.GenreData.builder()
                                .id(album.getGenre().getId())
                                .name(album.getGenre().getName())
                                .build()))
                        .build()
                        : null)
                .artistName(album.getArtist() != null ? album.getArtist().getName() : null)
                .releaseDate(album.getReleaseDate() != null ? album.getReleaseDate().toString(): null)
                .build();
    }

    public static Album toAlbumEntity(AlbumDTO albumDTO){
        if(albumDTO.getAlbumTitle() == null || albumDTO.getAlbumTitle().isBlank()){
            throw new IllegalArgumentException("Album title cannot be empty");
        }

        if(albumDTO.getReleaseDate() == null || albumDTO.getReleaseDate().isBlank()){
            throw new IllegalArgumentException("Release date cannot be empty");
        }



        Album.AlbumBuilder builder = Album.builder()
                .albumTitle(albumDTO.getAlbumTitle())
                .releaseDate(albumDTO.getReleaseDate() != null ? LocalDate.parse(albumDTO.getReleaseDate()) : null);

        if(albumDTO.getGenre() != null && albumDTO.getGenre().getData() != null && !albumDTO.getGenre().getData().isEmpty()) {
            GenreDTO.GenreData genreData = albumDTO.getGenre().getData().get(0);

            Genre genre = new Genre();
            genre.setName(genreData.getName());
            builder.genre(genre);
        } else{
            throw new IllegalArgumentException("Genre data cannot be empty");
        }

        if(albumDTO.getArtistName() != null && !albumDTO.getArtistName().isBlank()){
            Artist artist = new Artist();
            artist.setName(albumDTO.getArtistName());
            builder.artist(artist);

        }
        return builder.build();
    }

    public Album saveAlbumFromDeezer(int deezerAlbumId){
        try{
            AlbumDTO dto = DeezerService.fetchAlbumAsDto(deezerAlbumId);
            Album album = toAlbumEntity(dto);
            Album newAlbum = dao.create(album);
            return newAlbum;
        }catch(Exception e){
            throw new RuntimeException("Failed to save album from deezer api: " + e.getMessage());
        }
    }

    public static List<AlbumDTO> toAlbumDTOList(List<Album> albums){
        return albums.stream()
                .map(AlbumService::toAlbumDTO)
                .collect(Collectors.toList());
    }

    public static List<Album>  toAlbumEntityList(List<AlbumDTO> albumDTOs){
        return albumDTOs.stream()
                .map(AlbumService::toAlbumEntity)
                .collect(Collectors.toList());
    }
}
