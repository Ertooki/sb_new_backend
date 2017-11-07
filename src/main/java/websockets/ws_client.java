package websockets;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@ClientEndpoint
public class ws_client {
    Session userSession = null;
    String name;
    private MessageHandler messageHandler;

    public ws_client(String n) {
        name = n;
    }

    @OnOpen
    public void onOpen(Session userSession) {
        //main.infoLogger.info("opening "+name+" websocket");
        this.userSession = userSession;
        sendMessage(BCCommands.CONSTRUCT_SESSION.toString());
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket " + name+ " "+reason.getReasonPhrase()+ " " + reason.getCloseCode());
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author Jiji_Sasidharan
     */
    public static interface MessageHandler {

        public void handleMessage(String message);
    }
}

