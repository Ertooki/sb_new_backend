package main;

import lombok.Getter;
import org.json.simple.JSONObject;

import org.eclipse.jetty.websocket.api.Session;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal extends Thread {

    String id;
    String sbid;
    String multi;

    @Getter
    Session session;

    CountDownLatch latch;

    String active_gid;

    List<String> allowed_markets;
    List<String> main_markets;

    List<String> betslip;

    public Map<String,JSONObject> updates = new ConcurrentHashMap<>();

    @Getter
    public BlockingQueue<JSONObject> queue = new LinkedBlockingQueue<>();

    public Terminal(String id, String sbid, List<String> bets, String gid, String m, Session s){
        this.id = id;
        this.sbid = sbid;
        this.betslip = bets;
        this.active_gid = gid;
        this.multi = m;
        this.session = s;
    }

    public synchronized void setCount (int n) {
        this.latch = new CountDownLatch(n);
    }

    public void setGame(String gid) {
        this.active_gid = gid;
        System.out.println(this.id + " subscribing to game " + this.active_gid);
    }

    public void resetGame(){
        String gid = this.active_gid;
        this.active_gid = "";
        System.out.println(this.id + " unsubscribing from game " + gid + " and game_id is " + this.active_gid);
    }

    public void run(){
        try {
            while (!interrupted()) {
                if (!queue.isEmpty()) {
                    JSONObject qObj = queue.take();
                    String type = qObj.get("type").toString();
                    switch (type) {
                        case "start": {
                            setCount(4);
                            /*build_menu();
                            build_live();
                            build_flive();
                            build_favorite();*/
                            latch.await();
                            for (String uid : updates.keySet()) {
                                if (session.isOpen()) session.getRemote().sendString(updates.get(uid).toString());
                            }
                            updates.clear();
                        } break;
                        case "update": {
                            JSONObject payload = (JSONObject)qObj.get("data");
                            if(latch.getCount()>0) updates.put("",payload);
                            else {
                                if(session.isOpen()) session.getRemote().sendString(payload.toString());
                            }
                        } break;
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{

        }
    }
}
