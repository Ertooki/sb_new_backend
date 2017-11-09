package entity;

import org.json.simple.JSONObject;
import update.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Market {

    String id;
    String name;
    String type;
    String exp_id;
    Integer order;
    Double base;

    Map<String,Event> events = new ConcurrentHashMap<>();

    public Market(){

    }

    public Market(JSONObject obj){
        this.id = obj.get("id").toString();
        if(obj.containsKey("express_id")) this.exp_id = obj.get("express_id").toString();
        else this.exp_id = "";
        if(obj.containsKey("name")) this.name = obj.get("name").toString();
        else this.name = "No name";
        if(obj.containsKey("type")) this.type = obj.get("type").toString();
        else this.type = "No type";
        if(obj.containsKey("base")) this.base = Double.parseDouble(obj.get("base").toString());
        else this.base = 0.0;
        if(obj.containsKey("order")) this.order = Integer.parseInt(obj.get("order").toString());
        else this.order = 999;
        if(type == "OverUnder") System.out.println(id+" "+type+" "+base);
        JSONObject evnts = (JSONObject)obj.get("event");
        for(String eid : (Set<String>)evnts.keySet()){
            events.put(eid,new Event((JSONObject) evnts.get(eid)));
        }
    }

    public List<JSONObject> compare(Market m, String type){
        List<JSONObject> updates = new ArrayList<>();
        Set<String> keys1 = new ConcurrentHashMap<>(events).keySet();
        Set<String> keys2 =  new ConcurrentHashMap<>(m.events).keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            UpdateBuilder ub = new UpdateBuilder(type,"event","delete");
            updates.add(ub.genDelete(keys1));
        }

        for(String eid : m.events.keySet()) {
            if(events.containsKey(eid)) {
                updates.addAll(events.get(eid).compare(m.events.get(eid),type));
            }
        }
        return updates;
    }
}
