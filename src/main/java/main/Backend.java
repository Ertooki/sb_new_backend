package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.simple.JSONObject;
import update.Updater;
import websockets.BCCommands;
import websockets.ws_server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Backend {

    public static Map<String,Terminal> terminals = new ConcurrentHashMap<>();

    public static void main(String[] args){

        //Waiter
        CountDownLatch latch = new CountDownLatch(1);

        //Configuring and starting updaters
        JSONObject start = new JSONObject();
        start.put("type", "start");

        Updater soccer_updater = new Updater("1",latch, BCCommands.GET_LIVE_SOCCER.toString(), "live");
        soccer_updater.start();
        try {
            soccer_updater.queue.put(start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Configuring and starting ws server
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                //factory.getPolicy().setIdleTimeout(5000);
                factory.getPolicy().setAsyncWriteTimeout(10000);
                factory.register(ws_server.class);
            }
        };
        Server server = new Server(5025);
        server.setHandler(wsHandler);
        try {
            server.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
