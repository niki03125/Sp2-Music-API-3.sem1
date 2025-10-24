package app.services;


import app.daos.SongDAO;
import app.dtos.SongDTO;
import app.entities.Album;
import app.entities.Artist;
import app.entities.Song;

import javax.naming.Context;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SongService {
    private final SongDAO dao; //adgang til db
    private final DeezerService deezer;

    public SongService(SongDAO dao){
        this(dao,new DeezerService());
    }

    public SongService(SongDAO dao, DeezerService deezer){
        this.dao = dao;
        this.deezer = deezer;
    }

    //Create = lav ny sang
    public SongDTO create(SongDTO dto){
        //vi laver dto om til entity
        Song entity = toEntity(dto);
        Song created = dao.create(entity);
        return toDTO(created);
    }

    //Read = find en sang
    public SongDTO getById(int id){
        Song song = dao.getById(id);
        return (song != null) ? toDTO(song) : null; //retunere null hvis sangen ikke findes
    }

    //ReadAll = hent alle sange
    public List<SongDTO> getAll(){
        List<Song> songs = dao.getAll();
        return songs.stream()
                .map(SongService::toDTO)// songService i stedet for this, da den skal være statisk
                .collect(Collectors.toList());
    }

    //Update = Ændre en sang
    public SongDTO update(int id, SongDTO dto){
        //henter snag fra db
        Song existing = dao.getById(id);
        if(existing == null) return null;

        //kopier simple fleter fra dto til entity
        existing.setTitle(dto.getTitle());
        existing.setGenre(dto.getGenre());
        existing.setDuration(dto.getDuration());
        existing.setReleaseDate(LocalDate.parse(dto.getReleaseDate()));

        //hånter relationer
        if(dto.getAlbumId() != null){
            Album album = new Album();
            album.setAlbumId(dto.getAlbumId());
            existing.setAlbum(album);
        }else{
            existing.setAlbum(null);
        }

        if(dto.getArtistId() != null){
            Artist artist = new Artist();
            artist.setId(dto.getArtistId());
            existing.setArtist(artist);
        }

        Song updated = dao.update(existing);
        return toDTO(updated);
    }

    //Delete = fjern en sang
    public boolean delete(int id){
        return dao.delete(id);
    }

    public static SongDTO toDTO(Song song){
        if (song == null) return null;

        SongDTO dto = new SongDTO();
        dto.setId(song.getSongId());
        dto.setTitle(song.getTitle());
        dto.setGenre(song.getGenre());
        dto.setDuration(song.getDuration());
        dto.setReleaseDate(song.getReleaseDate().toString());
        dto.setArtistName(song.getArtist().getName());
        if(song.getArtist() != null){
            dto.setArtistName(song.getArtist().getName());
            dto.setArtistId(song.getArtist().getId());
        }
        dto.setAlbumId(song.getAlbum() != null
                //hvis sangen har et album, hent albumID
                ? song.getAlbum().getAlbumId()
                // hvis ik, sæt værdi til null
                :null);

         return dto;
    }

    public static Song toEntity(SongDTO dto){
        Song song = new Song();
        song.setSongId(dto.getId());
        song.setTitle(dto.getTitle());
        song.setGenre(dto.getGenre());
        song.setDuration(dto.getDuration());
        song.setReleaseDate(LocalDate.parse(dto.getReleaseDate()));
        //lav et Artis, sang skal altid have
        if(dto.getArtistId() != null){
            Artist artist = new Artist();
            artist.setId(dto.getArtistId());
            song.setArtist(artist);
        }
        if(dto.getAlbumId() != null){
            Album album = new Album();
            album.setAlbumId(dto.getAlbumId());
            song.setAlbum(album);
        }
        return song;
    }

    public SongDTO importFromDeezerTrack(long deezerTrackId, int localArtistId, Integer localAlbumId) {
        // 1) tjek først DB – for at undgå dobbelt-import
        Song existing = dao.findByDeezerTrackId(deezerTrackId);
        if (existing != null) {
            return toDTO(existing);
        }

        // 2) Hent fra Deezer
        SongDTO ext = deezer.fetchTrackAsDto(deezerTrackId);
        if (ext == null) throw new IllegalArgumentException("Could not fetch Deezer track: " + deezerTrackId);

        // 3) Sæt lokale relationer + defaults
        ext.setArtistId(localArtistId);
        ext.setAlbumId(localAlbumId);
        if (ext.getGenre() == null || ext.getGenre().isBlank()) ext.setGenre("Unknown");
        if (ext.getReleaseDate() == null || ext.getReleaseDate().isBlank()) ext.setReleaseDate("2000-01-01");
        if (ext.getDuration() <= 0) ext.setDuration(180);

        // 4) DTO -> Entity, sæt deezerTrackId, gem
        Song entity = toEntity(ext);
        entity.setDeezerTrackId(deezerTrackId);
        Song created = dao.create(entity);

        return toDTO(created);
    }

}
