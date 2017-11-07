package entity;

import org.json.simple.JSONObject;
import websockets.SBCommands;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Sport {

    Integer id;
    String name;
    String alias;
    Map<String,Region> regions = new ConcurrentHashMap<>();

    public Sport() {

    }

    public Sport(JSONObject obj) {
        this.id = Integer.parseInt(obj.get("id").toString());
        this.name = obj.get("name").toString();
        this.alias = obj.get("alias").toString();
        JSONObject regions = (JSONObject)obj.get("region");
        System.out.println(id+" "+alias+" "+name);
        for(String rid : (Set<String>)regions.keySet()) {
            regions.put(rid,new Region((JSONObject)regions.get(rid)));
        }
    }

    public void compare(Sport s) {
        Map<String,Region> r = s.regions;
        Set<String> keys1 = regions.keySet();
        Set<String> keys2 = r.keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            SBCommands.DELETE.generate();
        }
    }
}
