package app.services;

import app.daos.AlbumDAO;
import app.dtos.AlbumDTO;
import app.entities.Album;
import app.entities.Artist;

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
                .genre(album.getGenre())
                .releaseDate(album.getReleaseDate() != null ? album.getReleaseDate().toString(): null)
                .artistName(album.getArtist().getName())
                .build();
    }

    public static Album toAlbumEntity(AlbumDTO albumDTO){
        Artist artist = new Artist();
        return Album.builder()
                .albumId(albumDTO.getAlbumId())
                .albumTitle(albumDTO.getAlbumTitle())
                .genre(albumDTO.getGenre())
                .releaseDate(albumDTO.getReleaseDate() != null ? LocalDate.parse(albumDTO.getReleaseDate().toString()) : null)
                .artist(artist)
                .build();
    }
}
