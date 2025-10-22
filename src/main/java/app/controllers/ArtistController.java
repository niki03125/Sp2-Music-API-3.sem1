package app.controllers;

import app.entities.Artist;
import io.javalin.http.Context;

public class ArtistController implements IController<Artist,Integer>{
    @Override
    public void getById(Context ctx) {

    }

    @Override
    public void getAll(Context ctx) {

    }

    @Override
    public void create(Context ctx) {

    }

    @Override
    public void update(Context ctx) {

    }

    @Override
    public void delete(Context ctx) {

    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }

    @Override
    public Artist validateEntity(Context ctx) {
        return null;
    }
}
