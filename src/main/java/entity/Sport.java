package entity;

import org.json.simple.JSONObject;
import update.UpdateBuilder;

import java.util.*;
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
        JSONObject rgns = (JSONObject)obj.get("region");
        for(String rid : (Set<String>)rgns.keySet()) {
            regions.put(rid,new Region((JSONObject)rgns.get(rid)));
        }
    }

    public List<JSONObject> compare(Sport s, String type) {
        List<JSONObject> updates = new ArrayList<>();
        Map<String,Region> r = s.regions;
        Set<String> keys1 = new ConcurrentHashMap<>(regions).keySet();
        Set<String> keys2 =  new ConcurrentHashMap<>(r).keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            UpdateBuilder ub = new UpdateBuilder(type,"region","delete");
            updates.add(ub.genDelete(keys1));
        }
        for (String rid : r.keySet()){
            if(regions.containsKey(rid)){
                updates.addAll(regions.get(rid).compare(r.get(rid),type));
            }
            else {

            }

        }
        return updates;
    }
}
