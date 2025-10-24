package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", nullable = false, unique = true)
    private Integer albumId;

    @Column(nullable = false, name = "album_title")
    private String albumTitle;

    @JoinColumn(name = "genre_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Genre genre;

    @Column(nullable = false, name = "album_releaseDate")
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "album", cascade = {CascadeType.MERGE, CascadeType.PERSIST},fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    private Artist artist;


}
