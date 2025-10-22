package app.daos;

import app.entities.Artist;
import app.entities.Song;

import java.util.List;

public interface ISongDAO extends IDAO<Artist,Integer> {
    void addSong(Artist artist, Song song);
    void removeSong(Artist artist, Song song);
    List<Song> getSongsFromArtist(Artist artist);
}
