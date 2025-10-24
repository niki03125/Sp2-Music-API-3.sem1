package app.dtos;

import app.entities.Album;
import app.entities.Artist;
import app.entities.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumDTO {
    @JsonProperty("id")
    private Integer albumId;

    @JsonProperty("title")
    private String albumTitle;

    @JsonProperty("genre")
    private GenreDTO genre;

    @JsonProperty("releaseDate")
    private String releaseDate;

    @JsonProperty("name")
    private String artistName;

    @JsonProperty("nb_tracks")
    private int nbTracks;
}
