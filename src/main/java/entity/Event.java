package entity;

import org.json.simple.JSONObject;

public class Event {

    String id;
    String name;
    String type;
    Integer order;
    Double value;

    public Event() {

    }

    public Event(JSONObject obj) {
        this.id = obj.get("id").toString();
        if(obj.containsKey("name")) this.name = obj.get("name").toString();
        else this.name = "No Name";
        if(obj.containsKey("type")) this.type = obj.get("type").toString();
        else this.type = "No Type";
        if(obj.containsKey("price")) this.value = Double.parseDouble(obj.get("price").toString());
        else this.value = 0.0;
        if(obj.containsKey("order")) this.order = Integer.parseInt(obj.get("order").toString());
        else this.order = 999;
        System.out.println("\t\t\t\t\t"+id+" "+order+" "+type+" "+name+" "+value);
    }

    public boolean compare(Event e){
        if(this.value != e.value) {
        }
        return true;
    }
}
