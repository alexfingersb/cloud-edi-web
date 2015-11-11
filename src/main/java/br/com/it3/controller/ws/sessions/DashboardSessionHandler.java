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
import javax.json.JsonValue;
import javax.websocket.Session;

import br.com.it3.model.dao.impl.LoggerManager;
import br.com.it3.model.entities.MessageLog;
import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.User;
import br.com.it3.model.json.JsonUtil;

@ApplicationScoped
public class DashboardSessionHandler {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final Set<Session> sessions = new HashSet<>();
	LoggerManager manager = new LoggerManager();
	JsonUtil json = new JsonUtil();
	
	public void addSession(Session session) {
		if (sessions.add(session)) {
			logger.info("[download] Sessao " + session.getId() + " adicionada");
		} else {
			logger.info("[download] Sessao " + session.getId() + " ja existe");
		}
	}

	public void removeSession(Session session) {
		if (sessions.remove(session)) {
			logger.info(String.format("[download] session %s closed", session));
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
	
	
	public void listDashboard(Session session) {
		User user = (User)session.getUserProperties().get("user");
		
		logger.info("search files from user " + user);
		
        List<Object[]> sender   = manager.getLatestFilesSent(user.getId());
        List<Object[]> receiver = manager.getLatestFilesReceived(user.getId());
        
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
        			.add("FileName", obj[0].toString())
        			.add("FileLength", obj[1].toString())
        			.add("FileLastModified", formatDatetime((Timestamp)obj[2]))
        			.add("FileCrc", obj[3].toString())
        			.add("SentDate", formatDatetime((Timestamp)obj[4]))
        			.add("ReceivedDate", formatDatetime((Timestamp)obj[5]))
        			.add("User", obj[6].toString())
        			.build());
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
