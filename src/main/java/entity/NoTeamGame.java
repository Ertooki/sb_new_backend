package entity;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoTeamGame extends Game {

    public NoTeamGame() {

    }

    public NoTeamGame(JSONObject obj){
        super(obj);
        //System.out.println(obj);
        System.out.println("NoTeam game");
    }

    @Override
    public List<JSONObject> compare(Game g, String type){
        List<JSONObject> updates = new ArrayList<>();
        return updates;
    }
}
