package app.dtos;

import app.entities.Album;
import app.services.SongService;
import jakarta.persistence.SecondaryTable;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import app.entities.Artist;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArtistDTO {
    private Integer id;
    private String name;
    private List<SongDTO> songs;
    private List<AlbumDTO> albums;

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
                            .map(AlbumDTO::fromEntity)
                            .collect(Collectors.toList())
            );
        }
        return builder.build();
    }

    public Artist toEntity(){
        Artist artist = new Artist();

        if(this.id != null){
            artist.setId(this.id);
        }

        artist.setName(this.name);
        return artist;
    }
}
