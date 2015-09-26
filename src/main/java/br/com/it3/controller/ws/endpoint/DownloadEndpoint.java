package br.com.it3.controller.ws.endpoint;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.com.it3.controller.ws.sessions.ContextSessionHandler;
import br.com.it3.model.dao.impl.UserManager;
import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.RouteFrom;
import br.com.it3.model.entities.RouteTo;
import br.com.it3.model.entities.RouteUri;
import br.com.it3.model.entities.User;
import br.com.it3.model.entities.UserRoute;

@ApplicationScoped
@ServerEndpoint("/download")
public class DownloadEndpoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Inject
	private ContextSessionHandler sessionHandler;

	@OnOpen
	public void open(Session session) {
		sessionHandler.addSession(session);
	}
	
	@OnClose
	public void close(Session session) {
		sessionHandler.removeSession(session);
	}
	
	@OnError
	public void onError(Throwable error) {
		 Logger.getLogger(DownloadEndpoint.class.getName()).log(Level.SEVERE, null, error);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            String action = jsonMessage.getString("action");
            
            if ("list".equals(action)) {
            	sessionHandler.getRoutes(session);
            } else if ("listUsers".equals(action)) {
            	sessionHandler.listUsers(session);
            }
        }
	}
	
	private Route formRoute(JsonObject jsonMessage) {
		logger.info(jsonMessage.toString());
		
		Route route = new Route();
		route.setDescription(jsonMessage.getString("description"));
		
		RouteFrom from = new RouteFrom();
		from.setRoute(route);

		// Endereco do remetente
		RouteUri uriFrom = new RouteUri();
		uriFrom.setScheme(jsonMessage.getString("protocol"));
		uriFrom.setContextPath(jsonMessage.getString("cpath"));
		uriFrom.setOptions(jsonMessage.getString("options"));
		uriFrom.addRouteFrom(from);
		
		// Remetente
		User user = new UserManager().getUser(jsonMessage.getString("user"));
		UserRoute userRouteFrom = new UserRoute();
		userRouteFrom.setRoute(route);
		userRouteFrom.setUser(user);
		userRouteFrom.setRouteUri(uriFrom);
		route.addUserRoute(userRouteFrom);
		
		//Ler lista de rotas ḑestinatarias
		JsonObject jo = jsonMessage.getJsonObject("context");
		JsonArray ja = jo.getJsonArray("route");
		
		for (int i = 0; i < ja.size(); i++) {
			JsonObject jsonRoute = ja.getJsonObject(i);
			
			// Endereco do destinatario
			RouteUri uriTo = new RouteUri();
			uriTo.setScheme(jsonRoute.getString("protocol"));
			uriTo.setContextPath(jsonRoute.getString("cpath"));
			uriTo.setOptions(jsonRoute.getString("options"));
			
			User userto = new UserManager().getUser(jsonRoute.getString("user"));
			UserRoute userRouteTo = new UserRoute();
			userRouteTo.setRoute(route);
			userRouteTo.setUser(userto);
			userRouteTo.setRouteUri(uriTo);
			route.addUserRoute(userRouteTo);
			
			RouteTo to = new RouteTo();
			to.setChoiceWhen(jsonRoute.getString("filter"));
			to.setRouteUri(uriTo);
			
			uriTo.addRouteTo(to);
			from.addRouteTo(to);
			
		}
		route.setRouteFrom(from);
		return route;
	}
	
}
