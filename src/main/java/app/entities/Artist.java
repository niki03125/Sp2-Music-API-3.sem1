package app.entities;

import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.dtos.SongDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.awt.*;
import java.io.LineNumberInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id", nullable = false)
    private int id;

    @Column(name = "artist_name")
    private String name;

    @Column(name = "artist_age")
    private int age;

    @Column(name = "nationality")
    private String nationality;

   @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Song> songs = new HashSet<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Album> albums = new HashSet<>();

     public void addSongToArtist(Song song){
         if(song != null){
             this.songs.add(song);
             song.setArtist(this);
         }
     }

    public void addAlbum(Album album){
        if(album != null){
            this.albums.add(album);
            album.setArtist(this);
        }
    }
}
