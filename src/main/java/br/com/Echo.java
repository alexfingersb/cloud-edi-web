package br.com;

import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/echo")
public class Echo {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Session session;
	
	@OnOpen
	public void connect(Session session) {
		this.session = session;
		logger.info("/echo open connection with sesson id " + session.getId());
	}
	
	@OnClose
    public void close() {
        logger.info(String.format("/echo Session %s closed", session.getId()));
    }
	
	@OnMessage
	public void message(String message) {
		logger.info("/echo " + message);
		this.session.getAsyncRemote().sendText("Echo: " + message);
	}
}
