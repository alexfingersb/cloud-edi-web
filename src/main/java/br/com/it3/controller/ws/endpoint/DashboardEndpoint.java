package br.com.it3.controller.ws.endpoint;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.com.it3.controller.ws.sessions.DashboardSessionHandler;

@ApplicationScoped
@ServerEndpoint("/dashboard")
public class DashboardEndpoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Inject
	private DashboardSessionHandler sessionHandler;
	
	@OnOpen
	public void open(Session session) {
		logger.info("[dashboard] open connection with session" + session.getId());
		sessionHandler.addSession(session);
	}
	
	@OnClose
	public void close(Session session) {
		logger.info("[dashboard] close session");
		sessionHandler.removeSession(session);
	}
	
	@OnError
	public void onError(Throwable error) {
		logger.info("error: " + error.getMessage());
		 Logger.getLogger(DashboardEndpoint.class.getName()).log(Level.SEVERE, null, error);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("dashboard: " + message);
		
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
	        
			JsonObject jsonMessage = reader.readObject();
	        
			String action = jsonMessage.getString("action");

	        if ("list".equals(action)) {
	        	sessionHandler.listDashboard(session);
	        }
		}
		
	}
}
