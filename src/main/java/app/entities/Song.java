package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id", nullable = false, unique = true)
    private Integer songId;

    @Column(name = "song_title", nullable = false)
    private String title;

    @Column(name = "song_genre", nullable = false)
    private String genre;

    @Column(name = "duration",nullable = false)
    private int duration;

    @Column(name = "song_releaseDate", nullable = false)
    private LocalDate releaseDate;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "deezer_track_id", unique = true)
    private Long deezerTrackId;

    public Song(Artist artist, LocalDate releaseDate, int duration, String genre, String title) {
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genre = genre;
        this.title = title;
    }
}

