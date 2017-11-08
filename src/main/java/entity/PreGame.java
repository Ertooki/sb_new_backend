package entity;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreGame extends Game {

    public PreGame() {

    }

    public PreGame(JSONObject obj){
        super(obj);
        System.out.println("Prematch game");
    }

    @Override
    public List<JSONObject> compare(Game g, String type){
        List<JSONObject> updates = new ArrayList<>();
        return updates;
    }
}
