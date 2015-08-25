package br.com.it3.controller.ws.sessions;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import br.com.it3.model.dao.impl.UserManager;
import br.com.it3.model.entities.User;


@ApplicationScoped
public class UserSessionHandler {
//	private int userId = 0;
	private final Set<Session> sessions = new HashSet<>();
	private final Set<User> users = new HashSet<User>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private UserManager userDao = new UserManager();
	
	public void addSession(Session session) {
		if (sessions.add(session)) {
			logger.info("/user Sessao " + session.getId() + " adicionada");
		} else {
			logger.info("/user Sessao " + session.getId() + " ja existe");
		}
        for (User user : users) {
            JsonObject addMessage = createAddMessage(user);
            sendToSession(session, addMessage);
        }
	}

	public void removeSession(Session session) {
		if (sessions.remove(session)) {
			logger.info(String.format("/user session %s closed",session));
		}
	}

	public void getUsers(Session session) {
		for (User user : userDao.findAll()) {
			JsonObject addMessage = createListMessage(user);
            sendToSession(session, addMessage);
		}
	}

	public void addUser(User user) {
		logger.info("/user add user on session handler");
//		user.setId(userId);
//        users.add(user);
//        userId++;
		
        JsonObject addMessage = createAddMessage(user);
        sendToAllConnectedSessions(addMessage);
	}

	public void removeUser(int id) {
        User user = getUserById(id);
        if (user != null) {
            sessions.remove(user);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

	private User getUserById(int id) {
		return userDao.findById(id);
		
//        for (User user : users) {
//            if (user.getId() == id) {
//                return user;
//            }
//        }
//        return null;
    }

	private JsonObject createMessage(User user, String action) {
		JsonProvider provider = JsonProvider.provider();
		JsonObject addMessage = buildUserProvider(user, provider, action);
		return addMessage;
	}

	private JsonObject createAddMessage(User user) {
		return createMessage(user, "add");
	}

	private JsonObject createListMessage(User user) {
        return createMessage(user, "list");
    }

	private JsonObject buildUserProvider(User user, JsonProvider provider, String action) {
		return provider.createObjectBuilder()
                .add("action", action)
                .add("id", user.getId())
                .add("name", user.getName())
                .add("perfil", user.getProfile().toString())
                .add("status", user.getStatus())
                .add("login", user.getLogin())
                .add("password", user.getPassword())
                .add("email", user.getEmail())
                .build();
	}
    
    public void updateUser(User user) {
        JsonProvider provider = JsonProvider.provider();
        if (user != null) {
//            if ("On".equals(user.getStatus())) {
//                user.setStatus("Off");
//            } else {
//                user.setStatus("On");
//            }
            JsonObject updateUser = buildUserProvider(user, provider, "udpate");
            sendToAllConnectedSessions(updateUser);
        }
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
        	logger.info("/user Send to message to session " + session.getId());
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
        	logger.info("/user Send message " + message);
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(UserSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
