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
	private final Set<Session> sessions = new HashSet<>();
//	private final Set<User> users = new HashSet<User>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private UserManager userDao = new UserManager();
	
	public void addSession(Session session) {
		if (sessions.add(session)) {
			logger.info("[user' Sessao " + session.getId() + " adicionada");
		} else {
			logger.info("[user] Sessao " + session.getId() + " ja existe");
		}
//        for (User user : users) {
//            JsonObject addMessage = createAddMessage(user);
//            sendToSession(session, addMessage);
//        }
	}

	public void removeSession(Session session) {
		if (sessions.remove(session)) {
			logger.info(String.format("[user] session %s closed",session));
		}
	}

	public void getUsers(Session session) {
		for (User user : userDao.findAll()) {
			JsonObject addMessage = createListMessage(user);
            sendToSession(session, addMessage);
		}
	}

	public void addUser(User user, Session mysession) {
		userDao.persist(user);
        JsonObject addMessage = createAddMessage(user);
        sendToOtherConnectedSessions(addMessage, mysession);
	}

	public void removeUser(int id) {
        User user = getUserById(id);
        if (user != null) {
        	userDao.remove(user);
//            sessions.remove(user);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

	private User getUserById(long id) {
		User user = userDao.findById(id);
		return user;
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
                .add("profile", user.getProfile().toString())
                .add("status", user.getStatus())
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .add("email", user.getEmail())
                .build();
	}
    
    public void updateUser(User user, Session mysession) {
        JsonProvider provider = JsonProvider.provider();
        if (user != null) {
        	user = userDao.update(user);
            JsonObject updateUser = buildUserProvider(user, provider, "udpate");
            sendToOtherConnectedSessions(updateUser, mysession);
        }
    }
    
    public void editUser(long id, Session mysession) {
    	JsonProvider provider = JsonProvider.provider();
    	User user = getUserById(id);
    	JsonObject message = buildUserProvider(user, provider, "edit");
    	sendToSession(mysession, message);
    }

    private void sendToAllConnectedSessions(JsonObject message) {
    	for (Session session : sessions) {
    		sendToSession(session, message);
    	}
    }

    private void sendToOtherConnectedSessions(JsonObject message, Session mysession) {
        for (Session session : sessions) {
        	logger.info("Send message to all sessions " + session.getId());
        	if (session.getId() == mysession.getId()) continue;
            	sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(UserSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
