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

    public Song(String title, String genre, int duration, LocalDate releaseDate) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }
}

