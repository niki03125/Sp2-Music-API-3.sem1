package app;

import app.services.DeezerService;

public class Main {
    public static void main(String[] args) {
        DeezerService deezerService = new DeezerService();
        String json = deezerService.getInfoFromUrl("https://api.deezer.com/track/3135556");

        System.out.println(json);

        // TEST
    }

}