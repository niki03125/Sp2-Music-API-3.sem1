package app.services;

import app.daos.ArtistDAO;
import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.dtos.SongDTO;
import app.entities.Artist;
import app.entities.Song;
import org.eclipse.jetty.websocket.core.internal.messages.PartialStringMessageSink;

import java.util.List;
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
        return toDTO(created, true, true);
    }

    public ArtistDTO getById(int id){
        Artist artist = dao.getById(id);
        if(artist != null){
            return toDTO(artist, true, true);
        }else{
            return null;
        }
    }

    public List<ArtistDTO> getAll() {
        return dao.getAll().stream()
                .map(artist-> toDTO(artist, false, false))
                .collect(Collectors.toList());
    }

    public ArtistDTO update(int id, ArtistDTO dto){
        Artist exist = dao.getById(id);
        if(exist == null){
            return null;
        }

        exist.setName(dto.getName());

        Artist updated = dao.update(exist);
        return toDTO(updated, true, true);
    }



    public boolean delete(int id){
        return dao.delete(id);
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
                            .collect(Collectors.toSet())

            );
        }

        if(includeAlbums && artist.getAlbums() != null){
            builder.albums(
                    artist.getAlbums().stream()
                            .map(AlbumService::toAlbumDTO)
                            .collect(Collectors.toSet())
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
                   .collect(Collectors.toSet())
           );
       }


       if(dto.getAlbums() != null){
           artist.setAlbums(dto.getAlbums().stream()
                   .map(AlbumService::toAlbumEntity)
                   .collect(Collectors.toSet())
           );

       }

       return artist;
    }
}
