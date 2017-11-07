package entity;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    String id;
    String ext_id;
    String alias;
    String start;
    Integer type;

    Map<String,Market> markets = new ConcurrentHashMap<>();

    public Game() {

    }

    public Game(JSONObject obj){
        this.id = obj.get("id").toString();
        if(obj.containsKey("game_external_id")) this.ext_id = obj.get("game_external_id").toString();
        else this.ext_id = "";
        if(obj.containsKey("game_number")) this.alias = obj.get("game_number").toString();
        else this.alias = obj.get("id").toString();
        if(obj.containsKey("start_ts")) this.start = obj.get("start_ts").toString();
        else this.start = "";
        this.type = Integer.parseInt(obj.get("type").toString());
        System.out.println("\t\t\t"+id+" "+alias+" "+type);

        JSONObject mkts = (JSONObject)obj.get("market");
        for(String mid : (Set<String>)mkts.keySet()){
            mkts.put(mid,new Market((JSONObject)mkts.get(mid)));
        }
    }
}


