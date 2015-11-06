package br.com.it3.controller.ws.endpoint;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import br.com.it3.controller.ws.sessions.LoggerSessionHandler;
import br.com.it3.model.dao.impl.ContextManager;
import br.com.it3.model.dao.impl.LoggerManager;
import br.com.it3.model.entities.MessageLog;
import br.com.it3.model.entities.RouteFrom;
import br.com.it3.model.entities.RouteTo;

@ServerEndpoint ("/message")
public class LoggerEndpoint {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	LoggerManager manager = new LoggerManager();
	
	@Inject
	private LoggerSessionHandler sessionHandler;
	
	@OnOpen
	public void open(Session session) {
		logger.info("[download] open connection with session" + session.getId());
	}
	
	@OnClose
	public void close(Session session) {
		logger.info("[download] close session");
	}
	
	@OnError
	public void onError(Throwable error) {
		logger.info("error: " + error.getMessage());
		 Logger.getLogger(DownloadEndpoint.class.getName()).log(Level.SEVERE, null, error);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		
		System.out.println("message: " + message);
		
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			
			Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
			
            JsonObject jsonMessage = reader.readObject();
            String fileName = jsonMessage.getString("FileName");
            long fileLength = parseDouble(jsonMessage.getString("FileLength"));
            long fileDate   = parseDouble(jsonMessage.getString("FileLastModified"));
            long routeId 	= parseDouble(jsonMessage.getString("RouteFromId"));
            long crc 		= jsonMessage.getJsonNumber("crc32").longValue();
 
            
            MessageLog messageLog = manager.findByCrc(crc);
            
            if (messageLog == null) {
            	messageLog = new MessageLog();
            	// it is a new transfer
            	messageLog.setCrc(crc);
            	messageLog.setFileName(fileName);
            	messageLog.setFileLength(fileLength);
            	messageLog.setFileDate(new Timestamp(fileDate));
            	messageLog.setSentDate(now);
            	RouteFrom routeFrom = new ContextManager().getRouteFrom(routeId);
//            	routeFrom.addMessageLog(messageLog);
            	
            	messageLog.setRouteFrom(routeFrom);
            	manager.persist(messageLog);
            	
            } else {
            	// there has been a transfer
            	messageLog.setReceivedDate(now);
            	RouteTo routeTo = new ContextManager().getRouteTo(routeId);
//            	routeTo.addMessageLog(messageLog);
            	
            	messageLog.setRouteTo(routeTo);
            	manager.update(messageLog);
            }
            
        }
	}

	private long parseDouble(String value) {
		long num = 0;
		try {
			num = Long.valueOf(value);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			num = 0;
		}
		return num;
	}
	

}
