package entity;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    String id;
    String ext_id;
    String alias;
    String start;
    Integer type;
    Integer markets_count;
    Object exclude_ids;

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
        if(obj.containsKey("markets_count")) this.markets_count = Integer.parseInt(obj.get("markets_count").toString());
        else this.markets_count = 0;
        this.type = Integer.parseInt(obj.get("type").toString());
        if (obj.containsKey("exclude_ids") && obj.get("exclude_ids") != null) this.exclude_ids = obj.get("exclude_ids");

        JSONObject mkts = (JSONObject)obj.get("market");
        for(String mid : (Set<String>)mkts.keySet()){
            markets.put(mid,new Market((JSONObject)mkts.get(mid)));
        }
    }

    public List<JSONObject> compare(Game g, String type) {
        List<JSONObject> updates = new ArrayList<>(); return updates;
    }
}


