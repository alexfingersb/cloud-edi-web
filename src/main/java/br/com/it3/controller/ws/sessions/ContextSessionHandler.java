package br.com.it3.controller.ws.sessions;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import br.com.it3.model.dao.impl.ContextManager;
import br.com.it3.model.dao.impl.UserManager;
import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.User;
import br.com.it3.model.json.JsonUtil;

@ApplicationScoped
public class ContextSessionHandler {
	private final Set<Session> sessions = new HashSet<>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ContextManager contextDao = new ContextManager();

	public void addSession(Session session) {
		if (sessions.add(session)) {
			logger.info("[context] Sessao " + session.getId() + " adicionada");
		} else {
			logger.info("[context] Sessao " + session.getId() + " ja existe");
		}
	}

	public void removeSession(Session session) {
		if (sessions.remove(session)) {
			logger.info(String.format("[context] session %s closed", session));
		}
	}

	public void getRoutes(Session session) {
		for (Route route : contextDao.findAll()) {
			JsonObject addMessage = createListMessage(route);
			sendToSession(session, addMessage);
		}
	}

	public void addContext(Route route, Session mysession) {
		contextDao.persist(route);
		JsonObject addMessage = createAddMessage(route);
		sendToOtherConnectedSessions(addMessage, mysession);
	}

	public void removeUser(int id) {
		Route route = getRouteById(id);
		if (route != null) {
			contextDao.remove(route);
			JsonProvider provider = JsonProvider.provider();
			JsonObject removeMessage = provider.createObjectBuilder()
					.add("action", "remove").add("id", id).build();
			sendToAllConnectedSessions(removeMessage);
		}
	}

	public Route getRouteById(long id) {
		Route route = contextDao.findById(id);
		return route;
	}

	private JsonObject createMessage(Route route, String action) {
		JsonObject addMessage = buildRouteJson(route, action);
		return addMessage;
	}

	private JsonObject createAddMessage(Route route) {
		return createMessage(route, "add");
	}

	private JsonObject createListMessage(Route route) {
		return createMessage(route, "list");
	}

	public void updateRoute(Route route, Session mysession) {
		if (route != null) {
			route = contextDao.update(route);
			JsonObject updateUser = buildRouteJson(route, "udpate");
			sendToOtherConnectedSessions(updateUser, mysession);
		}
	}

	public void editRoute(long id, Session mysession) {
		Route route = getRouteById(id);
		JsonObject message = buildRouteJson(route, "edit");
		sendToSession(mysession, message);
	}

	private JsonObject buildRouteJson(Route route, String action) {
		return new JsonUtil().buildRouteJson(route, action);
	}

	private void sendToAllConnectedSessions(JsonObject message) {
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToOtherConnectedSessions(JsonObject message,
			Session mysession) {
		for (Session session : sessions) {
			logger.info("Send message to all sessions " + session.getId());
			if (session.getId() == mysession.getId())
				continue;
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, Object message) {
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			Logger.getLogger(ContextSessionHandler.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public void searchUser(final String name, Session mysession) {
		JsonUtil json = new JsonUtil();
		JsonArrayBuilder ja = Json.createArrayBuilder();

		for (User user : new UserManager().search(name)) {
			ja.add(json.buildUserJson(user, "searchUser"));
		}
		
		sendToSession(mysession, ja.build());
		
	}

	public void listUsers(Session mysession) {
		JsonUtil json = new JsonUtil();
		JsonArrayBuilder ja = Json.createArrayBuilder();

		for (User user : new UserManager().findAll()) {
			ja.add(json.buildUserJson(user, "listUsers"));
		}
		
		sendToSession(mysession, ja.build());
		
	}

	public void removeRote(int id) {
		Route route = contextDao.findById(id);
		contextDao.remove(route);
	}
}
