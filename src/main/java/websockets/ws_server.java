package websockets;

import main.Backend;
import main.Terminal;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ws_server {

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        ConcurrentHashMap<String,Terminal> terminals = (ConcurrentHashMap<String, Terminal>) Backend.terminals;
        String rm_tid = "";
        for(String tid : terminals.keySet())
        {
            Session rcpt = terminals.get(tid).getSession();
            if (rcpt == session)
            {
                rm_tid = tid;
                break;
            }
        }
        if (rm_tid != "") {
            Backend.terminals.get(rm_tid).interrupt();
            Backend.terminals.remove(rm_tid);
        }
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: ");
        t.printStackTrace();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        session.setIdleTimeout(600000);
        System.out.println("Connect: " + session.getRemoteAddress().getAddress() + "  - " +  new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    }

    @OnWebSocketMessage
    public void onMessage(Session s, String message) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject rcvd = (JSONObject) parser.parse(message);
            String comm = (String)rcvd.get("command");
            switch (comm) {
                case "build": {
                    System.out.println(rcvd+" "+s.getRemoteAddress());
                    String tid = rcvd.get("id").toString();
                    String sbid = "";
                    if (rcvd.containsKey("terminalId")) sbid = rcvd.get("terminalId").toString();
                    String multi = "1";
                    System.out.println("SB id is "+sbid);

                    Terminal t = new Terminal(tid, sbid, new ArrayList<String>(), "", multi, s);
                    Backend.terminals.put(tid, t);
                    t.start();
                    JSONObject start = new JSONObject();
                    start.put("type","start");
                    t.getQueue().put(start);
                } break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
