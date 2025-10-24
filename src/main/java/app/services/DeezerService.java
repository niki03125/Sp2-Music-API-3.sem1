package app.services;


import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.dtos.GenreDTO;
import app.dtos.SongDTO;
import app.entities.Artist;
import app.entities.Song;
import app.exceptions.ApiException;
import app.exceptions.NotFoundException;
import app.exceptions.ServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DeezerService {

    public String getInfoFromUrl(String url) {
        String result = null;

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                result = response.body();
            } else {
                System.out.println("Noget gik galt ved læsning af api " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //henter et track fra Deezer og mapper til en songDTO
    public SongDTO fetchTrackAsDto(long trackId) {
        SongDTO result = null;

        try {
            //opret HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // byg request
            String url = "https://api.deezer.com/track/" + trackId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .GET()
                    .build();

            //send request og få svar
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //hvis ok, parse Json og lave SongDTO

            if (response.statusCode() == 200) {
                String body = response.body();

                //Json parsing
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(body);

                //lav DTO og udfly felter
                SongDTO dto = new SongDTO();
                dto.setId(null); // id sættes i db
                dto.setTitle(node.path("title").asText(null));
                dto.setGenre("Unknown");
                dto.setDuration(node.path("duration").asInt(0));
                dto.setReleaseDate(node.path("release_date").asText("2000-01-01"));
                dto.setArtistName(node.path("artist").path("name").asText(null));

                result = dto;
            } else {
                System.out.println("Noget gik galt ved læsning af api. Statuskode: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //Get artist info from deezer and convert it to an ArtistDTO object
    public ArtistDTO fetchArtistAsDto(long artistId) throws ApiException {
        // Result Equals null if something has gone wrong
        ArtistDTO result = null;

        try {
            //Http client
            HttpClient client = HttpClient.newHttpClient();

            // Endpoint artist/id
            String url = "https://api.deezer.com/artist/" + artistId;

            // The get request object
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json") // We want would like a json response
                    .GET()
                    .build();

            // Sending response and receiving response as a string
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            // Is status code 200
            if (status == 200) {
                // To parse json
                ObjectMapper mapper = new ObjectMapper();

                // Reads json response as a tree of nodes
                JsonNode node = mapper.readTree(response.body());

                ArtistDTO artistDTO = new ArtistDTO();
                artistDTO.setId(null); // the id is handled by db
                artistDTO.setName(node.path("name").asText(null)); // get artist name from json
                artistDTO.setAlbumAmount(node.path("nb_album").asInt(0));
                artistDTO.setFanAmount(node.path("nb_fan").asInt(0));
                artistDTO.setTracklist(node.path("tracklist").asText(null));

                return artistDTO;
                // Artist was not found 404
            } else if (status == 404) {
                throw new NotFoundException("The artist was not found with id: " + artistId);
            } else if (status >= 500) {
                throw new ServerErrorException("The Deezer api returned server failed: " + response.statusCode());
            } else {
                // in general
                throw new ApiException(status, "Unexpected status code from the deezer api");
            }

            //IO, Interrupted, URISyntaxException samles
        } catch (Exception e) {
            throw new ApiException(500, " Failed at connection to deezer api: " + e.getMessage());
        }
    }

    public static AlbumDTO fetchAlbumAsDto(long albumId) throws ApiException {
        AlbumDTO result = null;


        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = "https://api.deezer.com/album/" + albumId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(body);

                AlbumDTO albumDTO = new AlbumDTO();
                albumDTO.setAlbumId(null);
                albumDTO.setAlbumTitle(node.path("title").asText(null));
                albumDTO.setReleaseDate(node.path("release_date").asText("2000-01-01"));
                albumDTO.setArtistName(node.path("artist").path("name").asText(null));
                albumDTO.setNbTracks(node.path("nb_tracks").asInt(0));

                JsonNode genreNode = node.path("genres").path("data");
                if(genreNode.isArray() && genreNode.size() > 0){
                    JsonNode genre = genreNode.path(0);
                    GenreDTO.GenreData genreData = new GenreDTO.GenreData();
                    genreData.setId(genre.path("id").asInt());
                    genreData.setName(genre.path("name").asText(null));

                    GenreDTO genreDTO = new GenreDTO();
                    genreDTO.setData(List.of(genreData));

                    albumDTO.setGenre(genreDTO);
                }

                result = albumDTO;
            } else {
                System.out.println("Noget gik galt ved læsning af api. Statuskode: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
