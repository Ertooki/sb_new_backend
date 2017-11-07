package websockets;

import org.json.simple.JSONObject;

import java.util.Set;

public enum SBCommands {

    DELETE {
        public JSONObject generate(String type, String what, Set<String> ids) {
            JSONObject obj = new JSONObject();
            obj.put("command","delete");
            obj.put("type", type);
            obj.put("what", what);
            obj.put("ids", ids);
            return obj;
        }
    },
    NEW{
        /*@Override
        public JSONObject generate() {

        }*/
    },
    UPDATE{
        /*@Override
        public JSONObject generate() {
            return  new JSONObject();
        }*/
    };

    //public abstract JSONObject generate();

    /*public JSONObject genJSON(JSONObject o) {
        JSONObject obj = new JSONObject();
        switch(this) {
            case NEW: obj.put("command","new"); break;
            case UPDATE: obj.put("command","update"); break;
            case DELETE: obj.put("command", "delete"); break;
            default: throw new AssertionError("Unknown command " + this);
        }
        return obj;
    }*/
}
