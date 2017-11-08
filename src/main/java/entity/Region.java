package entity;

import org.json.simple.JSONObject;
import update.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Region {

    String id;
    String name;
    String alias;
    Integer order;

    Map<String,Competition> comps = new ConcurrentHashMap<>();

    public Region(){

    }

    public Region(JSONObject obj){
        this.id = obj.get("id").toString();
        this.name = obj.get("name").toString();
        this.alias = obj.get("alias").toString();
        this.order = Integer.parseInt(obj.get("order").toString());
        JSONObject cmps = (JSONObject)obj.get("competition");
        for(String cid : (Set<String>)cmps.keySet()) {
            comps.put(cid,new Competition((JSONObject)cmps.get(cid)));
        }
    }

    public List<JSONObject> compare(Region r, String type){
        List<JSONObject> updates = new ArrayList<>();
        Map<String,Competition> c = r.comps;
        Set<String> keys1 = new ConcurrentHashMap<>(comps).keySet();
        Set<String> keys2 =  new ConcurrentHashMap<>(c).keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            UpdateBuilder ub = new UpdateBuilder(type,"comp","delete");
            updates.add(ub.genDelete(keys1));
        }
        for (String cid : c.keySet()){
            if(comps.containsKey(cid)){
                updates.addAll(comps.get(cid).compare(c.get(cid),type));
            }
            else {

            }

        }
        return updates;
    }
}
