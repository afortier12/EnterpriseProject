package ec.project.web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketServer {
	

	 private static final Set<Session> sessions = 
			  Collections.synchronizedSet(new HashSet<Session>());
  
	@OnOpen
    public void onOpen(Session session) throws IOException {
		sessions.add(session);
		System.out.println("Session added: " + session.toString());
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        sessions.remove(session);
        System.out.println("Session removed: " + session.toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
  
    public static void broadcastMessage(String message) {
    	for (Session s : sessions) {
    		if (s.isOpen()) {
    			try {
    				System.out.println(message);
    				s.getBasicRemote().sendText(message);
    			} catch (IOException e) {
						e.printStackTrace();
				}
    		}
    	}
    }
  
}