package app.dtos;

import app.entities.Album;
import app.entities.Artist;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AlbumDTO {
    private Integer albumId;
    private String albumTitle;
    private String genre;
    private String releaseDate;
    private String artistName;
    private int nbTracks;
    private String cover;


}
