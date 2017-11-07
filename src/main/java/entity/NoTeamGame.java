package entity;

import org.json.simple.JSONObject;

public class NoTeamGame extends Game {

    public NoTeamGame() {

    }

    public NoTeamGame(JSONObject obj){
        super(obj);
        System.out.println("NoTeam game");
    }
}
