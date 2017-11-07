package entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LiveGame extends Game {

    JSONObject team1 = new JSONObject();
    JSONObject team2 = new JSONObject();

    String state;
    String time;

    JSONArray live_events= new JSONArray();
    JSONObject last_event = new JSONObject();

    List<JSONObject> scoreboard = new ArrayList<>();

    public LiveGame() {

    }

    public LiveGame(JSONObject obj){
        super(obj);

        team1.put("name",obj.get("team1_name").toString());
        team2.put("name",obj.get("team2_name").toString());

        if(obj.containsKey("info")) {
            JSONObject info = (JSONObject) obj.get("info");

            if(info.containsKey("score1")) team1.put("score", info.get("score1"));
            if(info.containsKey("short1_color")) team1.put("short", info.get("short1_color").toString());
            if(info.containsKey("shirt1_color")) team1.put("shirt", info.get("shirt1_color").toString());

            if(info.containsKey("score2")) team2.put("score", info.get("score2"));
            if(info.containsKey("short2_color")) team2.put("short", info.get("short2_color").toString());
            if(info.containsKey("shirt2_color")) team2.put("shirt", info.get("shirt2_color").toString());

            if(info.containsKey("current_game_state")) this.state = info.get("current_game_state").toString();
            else this.state = "-";

            if(info.containsKey("current_game_time")) this.time = info.get("current_game_time").toString();
            else this.time = "0'";
        }

        if(obj.containsKey("live_events")) {
            this.live_events = (JSONArray)obj.get("live_events");
        }
        else this.live_events = new JSONArray();

        if(obj.containsKey("last_event")) {
            this.last_event = (JSONObject)obj.get("last_event");
        }
        else this.last_event = new JSONObject();

        if(obj.containsKey("stats")) {
            JSONObject stats = (JSONObject)obj.get("stats");
            for(String key : (Set<String>)stats.keySet()) {
                if(key.contains("score_set")) {
                    JSONObject score = new JSONObject();
                    JSONObject stat = (JSONObject)stats.get(key);
                    score.put("team1",stat.get("team1_value"));
                    score.put("team2",stat.get("team2_value"));
                    this.scoreboard.add(score);
                }
            }
        }

        System.out.println("\t\t\t"+time+" "+state+" "+team1+" "+team2+" "+scoreboard+"\n\t\t\t====================");
    }
}
