package entity;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Region {

    String id;
    String name;
    Integer order;

    Map<String,Competition> comps = new ConcurrentHashMap<>();

    public Region(){

    }

    public Region(JSONObject obj){
        this.id = obj.get("id").toString();
        this.name = obj.get("name").toString();
        this.order = Integer.parseInt(obj.get("order").toString());
        System.out.println("\t"+id+" "+name+" "+order);
        JSONObject comps = (JSONObject)obj.get("competition");
        for(String cid : (Set<String>)comps.keySet()) {
            comps.put(cid,new Competition((JSONObject)comps.get(cid)));
        }
    }
}
