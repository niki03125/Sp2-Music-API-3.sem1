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
    private Set<SongDTO> songs;
    private Set<AlbumDTO> albums;

}
