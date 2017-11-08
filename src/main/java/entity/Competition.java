package entity;

import org.json.simple.JSONObject;
import update.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;
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
        for(String gid : (Set<String>)gms.keySet()) {
            JSONObject game = (JSONObject)gms.get(gid);
            if (game.get("team2_name") != null && !game.get("team2_name").toString().isEmpty()) {
                if(game.get("type").toString().equals("1")) games.put(gid,new LiveGame(game));
                else games.put(gid,new PreGame(game));
            }
            else games.put(gid, new NoTeamGame(game));
        }
    }

    public List<JSONObject> compare(Competition c, String type){
        List<JSONObject> updates = new ArrayList<>();
        Map<String,Game> g = c.games;
        Set<String> keys1 = new ConcurrentHashMap<>(games).keySet();
        Set<String> keys2 =  new ConcurrentHashMap<>(g).keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            UpdateBuilder ub = new UpdateBuilder(type,"game","delete");
            updates.add(ub.genDelete(keys1));
        }
        for (String gid : g.keySet()){
            if(games.containsKey(gid)){
                updates.addAll(games.get(gid).compare(g.get(gid),type));
            }
            else {

            }

        }
        return updates;
    }
}
