package entity;

import org.json.simple.JSONObject;

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
        System.out.println("\t\t\t\t"+id+" "+type+" | "+name+" | "+base);
        JSONObject evnts = (JSONObject)obj.get("event");
        for(String eid : (Set<String>)evnts.keySet()){
            events.put(eid,new Event((JSONObject) evnts.get(eid)));
        }
    }
}
