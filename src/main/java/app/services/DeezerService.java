package app.services;


import app.dtos.SongDTO;
import app.entities.Song;
import app.exceptions.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DeezerService {

    public String getInfoFromUrl(String url){
        String result = null;

        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                result = response.body();
            } else{
                System.out.println("Noget gik galt ved læsning af api " + response.statusCode());
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    //henter et track fra Deezer og mapper til en songDTO
    public SongDTO fetchTrackAsDto(long trackId){
        SongDTO result = null;

        try{
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

            if(response.statusCode() == 200){
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
            }else {
                System.out.println("Noget gik galt ved læsning af api. Statuskode: " + response.statusCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
