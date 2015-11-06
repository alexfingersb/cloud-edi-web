package br.com.it3.controller.ws.sessions;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

import br.com.it3.model.dao.impl.ContextManager;
import br.com.it3.model.entities.Route;
import br.com.it3.model.json.JsonUtil;

@ApplicationScoped
public class LoggerSessionHandler {
	private ContextManager contextDao = new ContextManager();
	JsonUtil json = new JsonUtil();;

	public void getRoutes(Session session) {
		for (Route route : contextDao.findAll()) {
			JsonObject addMessage = createListMessage(route);
			sendToSession(session, addMessage);
		}
	}

	private JsonObject createMessage(Route route, String action) {
		JsonObject addMessage = buildRouteJson(route, action);
		return addMessage;
	}

	private JsonObject createListMessage(Route route) {
		return createMessage(route, "list");
	}

	private JsonObject buildRouteJson(Route route, String action) {
		return new JsonUtil().buildRouteJson(route, action);
	}

	private void sendToSession(Session session, Object message) {
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			Logger.getLogger(LoggerSessionHandler.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
}
