package app.services;
import app.daos.ArtistDAO;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import app.exceptions.ApiException;



import java.util.List;
import java.util.stream.Collectors;

public class ArtistService {
    private final ArtistDAO artistDAO;
    private final AlbumService albumService;
    private final SongService songService;

    public ArtistService(ArtistDAO artistDAO, AlbumService albumService, SongService songService){
        this.artistDAO = artistDAO;
        this.albumService = albumService;
        this.songService = songService;

    }

    public ArtistDTO create(ArtistDTO dto){
        //vi laver dto om til entity
        Artist entity = toEntity(dto);
        Artist created = artistDAO.create(entity);
        return toDTO(created, true, true);
    }

    public ArtistDTO getById(int id){
        Artist artist = artistDAO.getById(id);
        if(artist != null){
            return toDTO(artist, true, true);
        }else{
            return null;
        }
    }

    public List<ArtistDTO> getAll() {
        return artistDAO.getAll().stream()
                .map(artist-> toDTO(artist, false, false))
                .collect(Collectors.toList());
    }

    public ArtistDTO update(int id, ArtistDTO dto){
        Artist exist = artistDAO.getById(id);
        if(exist == null){
            return null;
        }

        exist.setName(dto.getName());

        Artist updated = artistDAO.update(exist);
        return toDTO(updated, true, true);
    }



    public boolean delete(int id){
        return artistDAO.delete(id);
    }



    public ArtistDTO toDTO(Artist artist, boolean includeSongs, boolean includeAlbums){
        if(artist == null) return null;

        var builder = ArtistDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .albumAmount(artist.getAlbumAmount())
                .fanAmount(artist.getFanAmount())
                .tracklist(artist.getTrackList());

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

    // Save artist to db
    public Artist saveArtistFromDeezer (long artistId) throws ApiException {

        DeezerService deezerService = new DeezerService();              
        // Get data from deezer
        ArtistDTO artistDTO = deezerService.fetchArtistAsDto(artistId);

        if(artistDTO == null || artistDTO.getName() == null){
            throw new ApiException(500, "Invalid artist data from returned from deezer");
        }

        // Convert from dto to entity
        Artist artist = new Artist();
        artist.setName(artistDTO.getName());
        artist.setAlbumAmount(artistDTO.getAlbumAmount());
        artist.setFanAmount(artistDTO.getFanAmount());
        artist.setTrackList(artistDTO.getTracklist());

        // save in db
        artistDAO.create(artist);

        return artist;
    }
}
