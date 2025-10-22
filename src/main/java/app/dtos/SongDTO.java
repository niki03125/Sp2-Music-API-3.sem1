package app.dtos;

import app.entities.Album;
import app.entities.Artist;
import app.entities.Song;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SongDTO {

    // Deezer track ID or internal song ID
    private Integer id;

    private String title;

    private double duration;

    private String genre;

    private String releaseDate;

    private String artistName;

    private Integer artistId;

    private Integer albumId;

}
