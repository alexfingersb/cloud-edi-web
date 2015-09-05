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

import br.com.it3.controller.ws.sessions.ContextSessionHandler;
import br.com.it3.model.entities.Route;

@ApplicationScoped
@ServerEndpoint("/context")
public class ContextEndpoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Inject
	private ContextSessionHandler sessionHandler;

	@OnOpen
	public void open(Session session) {
		logger.info("[context] open connection with sesson id " + session.getId());
		sessionHandler.addSession(session);
	}
	
	@OnClose
	public void close(Session session) {
		logger.info(String.format("[context] Session %s closed", session.getId()));
		sessionHandler.removeSession(session);
	}
	
	@OnError
	public void onError(Throwable error) {
		 Logger.getLogger(ContextEndpoint.class.getName()).log(Level.SEVERE, null, error);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            String action = jsonMessage.getString("action");
            
            logger.info("[context] action="+action);
            
            if ("add".equals(action)) {
                Route route = formRoute(jsonMessage);
                sessionHandler.addContext(route, session);
            } else if ("remove".equals(action)) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeUser(id);
            } else if ("update".equals(action)) {
                Route route = formRoute(jsonMessage);
                sessionHandler.updateRoute(route, session);
            } else if ("list".equals(action)) {
            	sessionHandler.getRoutes(session);
            } else if ("edit".equals(action)) {
            	int id = (int) jsonMessage.getInt("id");
            	sessionHandler.editRoute(id, session);
            } else if ("searchUser".equals(action)) {
            	String userName = jsonMessage.getString("name");
            	sessionHandler.searchUser(userName, session);
            }
        }
	}
	
	private Route formRoute(JsonObject jsonMessage) {
		Route route = new Route();
		route.setDescription(jsonMessage.getString("description"));
        logger.info(jsonMessage.toString());
		return route;
	}
	
}
