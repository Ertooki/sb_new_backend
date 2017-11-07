package entity;

import org.json.simple.JSONObject;

public class PreGame extends Game {

    public PreGame() {

    }

    public PreGame(JSONObject obj){
        super(obj);
        System.out.println("Prematch game");
    }
}
