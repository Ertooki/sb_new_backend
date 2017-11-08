package entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import update.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
            int yt1 = 0, yt2 = 0, rt1 = 0, rt2 = 0;
            for (Object live_event : live_events)
            {
                JSONObject le = (JSONObject)live_event;
                if (le.containsKey("event_type"))
                {
                    if (((String)le.get("event_type")).equals("yellow_card"))
                    {
                        switch ((String)le.get("team"))
                        {
                            case "team1": yt1++; break;
                            case "team2": yt2++; break;
                        }
                    }
                    if (((String)le.get("event_type")).equals("red_card"))
                    {
                        switch ((String)le.get("team"))
                        {
                            case "team1": rt1++; break;
                            case "team2": rt2++; break;
                        }
                    }
                }
            }
            this.team1.put("red_card", rt1);
            this.team1.put("yel_card", yt1);

            this.team2.put("red_card", rt2);
            this.team2.put("yel_card", yt2);
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
    }

    @Override
    public List<JSONObject> compare(Game g, String type){
        List<JSONObject> updates = new ArrayList<>();
        //Checking markets count
        if(!Objects.equals(this.markets_count, g.markets_count)) {
            UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
            updates.add(ub.genUpdate(this.id,"mc",g.markets_count));
        }
        //Checking game state
        if(this.state == null ||
                (this.state != null && !Objects.equals(this.state, ((LiveGame) g).state))) {
            UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
            updates.add(ub.genUpdate(this.id,"state",((LiveGame) g).state));
        }

        //Checking game time
        if(this.time == null ||
                (this.time != null && !Objects.equals(this.time, ((LiveGame) g).time))){
            UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
            updates.add(ub.genUpdate(this.id,"time",((LiveGame) g).time));
        }

        //Checking team params
        updates.addAll(check_team(this.team1,((LiveGame) g).team1,"1",type,updates));
        updates.addAll(check_team(this.team2,((LiveGame) g).team2,"2",type,updates));

        Set<String> keys1 = new ConcurrentHashMap<>(markets).keySet();
        Set<String> keys2 =  new ConcurrentHashMap<>(g.markets).keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            UpdateBuilder ub = new UpdateBuilder(type,"market","delete");
            updates.add(ub.genDelete(keys1));
        }

        for(String mid : g.markets.keySet()){
            if(this.markets.containsKey(mid)) {
                updates.addAll(markets.get(mid).compare(g.markets.get(mid),type));
            }
        }

        return updates;
    }

    List<JSONObject> check_team(JSONObject t1, JSONObject t2, String team, String type, List<JSONObject> updates){
        //Checking score
        if(t1.containsKey("score") && t2.containsKey("score")){
            Integer currScore = Integer.parseInt(t1.get("score").toString());
            Integer newScore = Integer.parseInt(t2.get("score").toString());
            if(!Objects.equals(newScore, currScore)) {
                UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
                updates.add(ub.genUpdate(this.id,"score"+team,t2.get("score")));
            }
        }
        else if (!t1.containsKey("score") && t2.containsKey("score")){
            UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
            updates.add(ub.genUpdate(this.id,"score"+team,t2.get("score")));
        }
        //Checking red cards
        if(t1.containsKey("red_card") && t2.containsKey("red_card")){
            Integer currRed = Integer.parseInt(t1.get("red_card").toString());
            Integer newRed = Integer.parseInt(t2.get("red_card").toString());
            if(!Objects.equals(newRed, currRed)) {
                UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
                updates.add(ub.genUpdate(this.id,"red_"+team,t2.get("red_card")));
            }
        }
        else if (!t1.containsKey("red_card") && t2.containsKey("red_card")){
            UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
            updates.add(ub.genUpdate(this.id,"red_"+team,t2.get("red_card")));
        }
        //Checking yellow cards
        if(t1.containsKey("yel_card") && t2.containsKey("yel_card")){
            Integer currYel = Integer.parseInt(t1.get("yel_card").toString());
            Integer newYel = Integer.parseInt(t2.get("yel_card").toString());
            if(!Objects.equals(newYel, currYel)) {
                UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
                updates.add(ub.genUpdate(this.id,"yel_"+team,t2.get("yel_card")));
            }
        }
        else if (!t1.containsKey("yel_card") && t2.containsKey("yel_card")){
            UpdateBuilder ub = new UpdateBuilder(type, "game", "update");
            updates.add(ub.genUpdate(this.id,"yel_"+team,t2.get("yel_card")));
        }
        return updates;
    }
}
