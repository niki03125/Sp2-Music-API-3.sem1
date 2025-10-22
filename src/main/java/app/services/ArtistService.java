package app.services;

import app.daos.ArtistDAO;
import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import app.entities.Song;

import java.util.stream.Collectors;

public class ArtistService {
    private final ArtistDAO dao;
    private final AlbumService albumService;
    private final SongService songService;

    public ArtistService(ArtistDAO dao, AlbumService albumService, SongService songService){
        this.dao = dao;
        this.albumService = albumService;
        this.songService = songService;
    }

    public ArtistDTO create(ArtistDTO dto){
        //vi laver dto om til entity
        Artist entity = toEntity(dto);
        Artist created = dao.create(entity);
        return toDTO(created);
    }

    public ArtistDTO toDTO(Artist artist, boolean includeSongs, boolean includeAlbums){
        if(artist == null) return null;

        var builder = ArtistDTO.builder()
                .id(artist.getId())
                .name(artist.getName());

        if(includeSongs && artist.getSongs() != null){
            builder.songs(
                    artist.getSongs().stream()
                            .map(SongService::toDTO)
                            .collect(Collectors.toList())

            );
        }

        if(includeAlbums && artist.getAlbums() != null){
            builder.albums(
                    artist.getAlbums().stream()
                            .map(AlbumService::toAlbumDTO)
                            .collect(Collectors.toList())
            );
        }
        return builder.build();
    }

    public Artist toEntity(ArtistDTO dto){
       if(dto == null) return null;

       Artist artist = new Artist();

       if(dto.getId() != null){
           artist.setId(dto.getId());
       }

       artist.setName(dto.getName());

       if(dto.getSongs() != null){
           artist.setSongs(dto.getSongs().stream()
                   .map(SongService::toEntity)
                   .collect(Collectors.toList()));
       }


       if(dto.getAlbums() != null){
           artist.setAlbums(dto.getAlbums().stream()
                   .map(AlbumService::toAlbumEntity)
                   .collect(Collectors.toList())

           );
       }

       return artist;
    }
}
