package update;

import entity.Sport;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import websockets.ws_client;

import java.net.URI;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.glassfish.tyrus.client.ClientManager;

public class Updater extends Thread {

    public Map<String, Sport> data= new ConcurrentHashMap<>();
    public BlockingQueue<JSONObject> queue = new LinkedBlockingQueue<>();

    private ws_client client;
    private boolean updateFlag = false;
    private CountDownLatch latch = null;
    private String command;
    private String port;
    private String type;

    public Updater(String p, CountDownLatch l, String c, String t) {
        this.port = p;
        this.latch = l;
        this.command = c;
        this.type = t;

        client = new ws_client("update.Updater thread");

        client.addMessageHandler(message -> {
            JSONParser parser = new JSONParser();
            JSONObject rcvd;
            try
            {
                rcvd = (JSONObject) parser.parse(message);
                //System.out.println(rcvd);
                if (rcvd.containsKey("rid")) {
                    if (!rcvd.get("rid").toString().isEmpty()) {
                        if(rcvd.get("data").getClass().getName().equals("java.lang.String")) {
                            JSONObject tdata = new JSONObject();
                            tdata.put("type", "data");
                            tdata.put("id",rcvd.get("rid"));
                            tdata.put("data", new JSONObject());
                            queue.put(tdata);
                        }
                        else {
                            JSONObject data = (JSONObject) rcvd.get("data");
                            if (data.containsKey("data")) {
                                JSONObject data2 = (JSONObject) data.get("data");
                                JSONObject tdata = new JSONObject();
                                tdata.put("type", "data");
                                tdata.put("id",rcvd.get("rid"));
                                tdata.put("data", data2);
                                queue.put(tdata);
                            }
                        }

                    }
                }

            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void run() {
        try {
            while (!interrupted()) {
                if (!queue.isEmpty()) {
                    JSONObject qd = queue.take();
                    switch ((String) qd.get("type")) {
                        case "start": {
                            System.out.println(command);
                            ClientManager cm = ClientManager.createClient();
                            cm.connectToServer(client, new URI("wss://eu-swarm-test.betconstruct.com"));
                            client.sendMessage(command);
                        } break;
                        case "data": {
                            JSONObject queueData = (JSONObject)qd.get("data");
                            if(queueData.containsKey("sport")) {
                                JSONObject sports = (JSONObject) queueData.get("sport");
                                if (updateFlag) update(sports);
                                else build(sports);
                            }
                            else client.sendMessage(command);
                        } break;
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {

        }
    }

    private void build(JSONObject sports) {

        for(String sid : (Set<String>)sports.keySet()) {
            data.put(sid,new Sport((JSONObject)sports.get(sid)));
        }

    }

    private void update(JSONObject sports) {
        Map<String,Sport> newData = new ConcurrentHashMap<>();

        for(String sid : (Set<String>)sports.keySet()) {
            newData.put(sid,new Sport((JSONObject)sports.get(sid)));
        }

        List<JSONObject> updates = new ArrayList<>();

        Set<String> keys1 = data.keySet();
        Set<String> keys2 = newData.keySet();
        keys1.removeAll(keys2);
        if(keys1.size()>0){
            UpdateBuilder ub = new UpdateBuilder(type,"sport","delete");
            updates.add(ub.genDelete(keys1));
        }

        for(String sid : newData.keySet()){
            if(data.containsKey(sid)){
                data.get(sid).compare(newData.get(sid),type);
            }
            else {
                newData.get(sid);
            }
        }
    }
}
