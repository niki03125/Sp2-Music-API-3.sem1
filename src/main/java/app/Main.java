package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.services.DeezerService;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(7007);
        DeezerService deezerService = new DeezerService();
        String json = deezerService.getInfoFromUrl("https://api.deezer.com/track/3135556");

        System.out.println(json);
        //

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    }

}