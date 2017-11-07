package entity;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Competition {

    String id;
    String name;
    Map<String,Game> games = new ConcurrentHashMap<>();

    public Competition(){

    }

    public Competition(JSONObject obj) {
        this.id = obj.get("id").toString();
        this.name = obj.get("name").toString();
        JSONObject gms = (JSONObject)obj.get("game");
        System.out.println("\t\t"+id+" "+name);
        for(String gid : (Set<String>)gms.keySet()) {
            JSONObject game = (JSONObject)gms.get(gid);
            if (game.get("team2_name") != null && !game.get("team2_name").toString().isEmpty()) {
                if(game.get("type").toString().equals("1")) gms.put(gid,new LiveGame(game));
                else gms.put(gid,new PreGame(game));
            }
            else gms.put(gid, new NoTeamGame(game));
        }
    }
}
