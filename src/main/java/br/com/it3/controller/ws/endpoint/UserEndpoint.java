package br.com.it3.controller.ws.endpoint;

import java.io.StringReader;
import java.util.List;
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

import br.com.it3.controller.ws.sessions.UserSessionHandler;
import br.com.it3.model.entities.User;
import br.com.it3.model.enums.Profile;

@ApplicationScoped
@ServerEndpoint("/user")
public class UserEndpoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Inject
	private UserSessionHandler sessionHandler;

	@OnOpen
	public void open(Session session) {
		logger.info("/user open connection with sesson id " + session.getId());
		sessionHandler.addSession(session);
	}
	
	@OnClose
	public void close(Session session) {
		logger.info(String.format("Session %s closed", session.getId()));
		sessionHandler.removeSession(session);
	}
	
	@OnError
	public void onError(Throwable error) {
		 Logger.getLogger(UserEndpoint.class.getName()).log(Level.SEVERE, null, error);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                User user = formUser(jsonMessage);
                logger.info("adiciona usuario na sessao " + user.getName());
                sessionHandler.addUser(user, session);
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeUser(id);
                logger.info("usuario removido " + id);
            }

            if ("updte".equals(jsonMessage.getString("action"))) {
                User user = formUser(jsonMessage);
                sessionHandler.updateUser(user, session);
                logger.info("user atualizado " + user.getName());
            }
            if ("list".equals(jsonMessage.getString("action"))) {
            	sessionHandler.getUsers(session);
            }
        }
	}
	
	private User formUser(JsonObject jsonMessage) {
		User user = new User();
        user.setName(jsonMessage.getString("name"));
        user.setEmail(jsonMessage.getString("email"));
        user.setProfile(Profile.valueOf(jsonMessage.getString("profile"))); //TODO Validar constante Perfil
        user.setUsername(jsonMessage.getString("username"));
        user.setPassword(jsonMessage.getString("password")); //TODO Criprografar senha com MD5
        user.setStatus(jsonMessage.getString("status"));
		return user;
	}
	
}
