package br.com.it3.controller.ws.sessions;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import br.com.it3.model.dao.impl.LoggerManager;
import br.com.it3.model.dao.impl.UserManager;
import br.com.it3.model.entities.User;
import br.com.it3.model.json.JsonUtil;


@ApplicationScoped
public class UserSessionHandler {
	private final static Set<Session> sessions = new HashSet<>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private UserManager usserManager = new UserManager();
	LoggerManager loggerManager = new LoggerManager();
	
	public void addSession(Session session) {
		if (sessions.add(session)) {
			logger.info("[user] Sessao " + session.getId() + " adicionada");
		} else {
			logger.info("[user] Sessao " + session.getId() + " ja existe");
		}
	}

	public void removeSession(Session session) {
		if (sessions.remove(session)) {
			logger.info(String.format("[user] session %s closed",session));
		}
	}

	public void getUsers(Session session) {
		for (User user : usserManager.findAll()) {
			JsonObject addMessage = createListMessage(user);
            sendToSession(session, addMessage);
		}
	}

	public void addUser(User user, Session mysession) {
		usserManager.persist(user);
        JsonObject addMessage = createAddMessage(user);
        sendToOtherConnectedSessions(addMessage, mysession);
	}

	public void removeUser(int id) {
        User user = getUserById(id);
        if (user != null) {
        	usserManager.remove(user);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

	private User getUserById(long id) {
		User user = usserManager.findById(id);
		return user;
    }

	private JsonObject createMessage(User user, String action) {
		JsonObject addMessage = buildUserJson(user, action);
		return addMessage;
	}

	private JsonObject createAddMessage(User user) {
		return createMessage(user, "add");
	}

	private JsonObject createListMessage(User user) {
        return createMessage(user, "list");
    }

    
    public void updateUser(User user, Session mysession) {
        if (user != null) {
        	user = usserManager.update(user);
            JsonObject updateUser = buildUserJson(user, "udpate");
            sendToOtherConnectedSessions(updateUser, mysession);
        }
    }
    
    private JsonObject buildUserJson(User user, String action) {
		return new JsonUtil().buildUserJson(user, action);
	}

	public void editUser(long id, Session mysession) {
    	User user = getUserById(id);
    	JsonObject message = buildUserJson(user, "edit");
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

    private void sendToSession(Session session, Object message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(UserSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	public void doLogin(String username, String password, Session session) {
		User login = usserManager.login(username, password);
		
		if (login != null) {
			logger.info("add user properties to session_id=" + session.getId() + " and user=" + login.getId());
			session.getUserProperties().put("user", login);
			
			User user = (User)session.getUserProperties().get("user");
			logger.info("user session " + user.getId());
			sessions.remove(session);
			sessions.add(session);
			sendToSession(session, Json.createObjectBuilder().add("action", "loginOk").build());
		} else {
			sendToSession(session, Json.createObjectBuilder().add("action", "loginError").build());
		}
	}
	
	/* The new Dashboard */
	public void listDashboard(Session session) {
		logger.info("get user properties to session_id=" + session.getId());
		User user = (User)session.getUserProperties().get("user");
		
		logger.info("search files from user " + user);
		
        List<Object[]> sender   = loggerManager.getLatestFilesSent(user.getId());
        List<Object[]> receiver = loggerManager.getLatestFilesReceived(user.getId());
        
        JsonArrayBuilder jaSender   = Json.createArrayBuilder();
        JsonArrayBuilder jaReceiver = Json.createArrayBuilder();
        
        
        
        for (Object obj[]: sender) {
        	jaSender.add(Json.createObjectBuilder()
        			.add("FileName", obj[0].toString())
        			.add("FileLength", obj[1].toString())
        			.add("FileLastModified", formatDatetime((Timestamp)obj[2]))
        			.add("FileCrc", obj[3].toString())
        			.add("SentDate", formatDatetime((Timestamp)obj[4]))
        			.add("ReceivedDate", formatDatetime((Timestamp)obj[5]))
        			.add("User", obj[6].toString())
        			.build());
        }

        for (Object obj[]: receiver) {
        	jaReceiver.add(Json.createObjectBuilder()
        			.add("FileName", (obj[0] == null ? "" : obj[0].toString()))
        			.add("FileLength", (obj[1] == null ? "" : obj[1].toString()))
        			.add("FileLastModified", (obj[2] == null ? "" : formatDatetime((Timestamp)obj[2])))
        			.add("FileCrc", (obj[3] == null ? "" : obj[3].toString()))
        			.add("SentDate", (obj[5] == null ? "" : formatDatetime((Timestamp)obj[4])))
        			.add("ReceivedDate", (obj[5] == null ? "" : formatDatetime((Timestamp)obj[5])))
        			.add("User", (obj[6] == null ? "" : obj[6].toString())))
        			.build();
        }
        
        JsonObject ja = Json.createObjectBuilder()
        		.add("sender", jaSender)
        		.add("receiver", jaReceiver)
        		.build();
        
        logger.info(ja.toString());
        
        sendToSession(session, ja.toString());
    }

	private String formatDatetime(Timestamp date) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(date);
	}
}
