package app.services;


import java.net.URI;
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

}
