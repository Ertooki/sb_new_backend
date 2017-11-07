package websockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Date;

@WebSocket
public class ws_server {

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {

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

    }
}
