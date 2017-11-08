package entity;

import org.json.simple.JSONObject;
import update.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

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
    }

    public List<JSONObject> compare(Event e, String type){
        List<JSONObject> updates = new ArrayList<>();
        if(!this.value.equals(e.value)) {
            UpdateBuilder ub = new UpdateBuilder(type, "event", "update");
            updates.add(ub.genUpdate(this.id,"price",e.value));
        }
        return updates;
    }
}
