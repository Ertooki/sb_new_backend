package update;

import org.json.simple.JSONObject;

import java.util.Set;

public class UpdateBuilder<T> {

    JSONObject update;

    public UpdateBuilder(String t, String w, String c){
        this.update = new JSONObject();
        this.update.put("command", c);
        this.update.put("type", t);
        this.update.put("what", w);
    }

    public JSONObject genDelete(Set<String> ids){
        this.update.put("ids",ids);
        return this.update;
    }

    public JSONObject genUpdate(String id, String param, T value){
        this.update.put("id",id);
        this.update.put("param",param);
        this.update.put("value",value);
        return this.update;
    }
}
