package br.com.it3.controller.ws.sessions;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.com.it3.model.dao.impl.ContextManager;
import br.com.it3.model.dao.impl.UserManager;
import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.RouteFrom;
import br.com.it3.model.entities.RouteTo;
import br.com.it3.model.entities.User;
import br.com.it3.model.json.JsonUtil;
import br.com.it3.model.xml.Address;
import br.com.it3.model.xml.Beans;
import br.com.it3.model.xml.CamelContext;
import br.com.it3.model.xml.Choice;
import br.com.it3.model.xml.When;

@ApplicationScoped
public class DownloadSessionHandler {
	private static final String AGENT_ZIP = "agent.zip";
	private final Set<Session> sessions = new HashSet<>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ContextManager contextDao = new ContextManager();
	List<String> fileList;
	JsonUtil json = new JsonUtil();;

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
			sessions.remove(session);
			Logger.getLogger(DownloadSessionHandler.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public void listUsers(Session mysession) {
		JsonArrayBuilder ja = Json.createArrayBuilder();

		for (User user : new UserManager().findAll()) {
			ja.add(json.buildUserJson(user, "listUsers"));
		}

		sendToSession(mysession, ja.build());

	}

	public void download(JsonObject message, Session mysession) {
		
		int id = (int) message.getInt("id");
		String system = message.getString("system");
		
		Route route = contextDao.findById(id);
		JsonObject jo = buildRouteJson(route, "download");
		try {
			prepareDownload(id, system, jo, mysession);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareDownload(int id, String system, JsonObject jo, Session mysession) throws Exception {
		// copy files to download directory
		prepareFilesToDownload(id, system);
		
		// gerar xml do contexto
		logger.info("gerando xml para " + jo);
		Route route = contextDao.findById(id);
		generateRouteXml(route);
		
		// preparar arquivo para download
		Path tmp = generateZip(id);
		
		JsonObject obj = json.buildDownloadJson(id, tmp.toString(), "download");
		logger.info(obj.toString());
		sendToSession(mysession, obj.toString());
	}

	private void prepareFilesToDownload(int id, String system) {
		logger.info("copiando arquivos para download...");
		
		File agent = new File(getClass().getResource("/download/agent").getFile());
		File ext = new File(getClass().getResource("/download/system/" + system).getFile());
		File output = new File(downloadDirectory() + id);
		
		try {
			FileUtils.copyDirectory(agent, output);
			FileUtils.copyDirectory(ext, output);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void generateRouteXml(Route entity) throws Exception {
		RouteFrom routeFrom = entity.getRouteFrom();
		Address from = new Address();
        from.setUri(routeFrom.getRouteUri().toString());
        
        
        br.com.it3.model.xml.Route route = new br.com.it3.model.xml.Route();
        route.setId(String.valueOf(routeFrom.getId()));
        route.setFrom(from);

        for (RouteTo routeTo : entity.getRouteFrom().getRouteTo()) {
        	Address to = new Address();
        	String choiceWhen = routeTo.getChoiceWhen();
        	to.setUri(routeTo.getRouteUri().toString());
        	
        	if (choiceWhen != null) {
        		Choice choice = new Choice();
	        	When when = new When();
	        	when.setTo(to);
	        	when.setSimple(routeTo.getChoiceWhen());
	        	choice.setWhen(when);
	        	route.setChoice(choice);
        	} else {
        		to.setUri(routeTo.getRouteUri().toString());
        		route.setTo(to);
        	}
		}

        
        CamelContext context = new CamelContext();
        context.addRoute(route);
		Beans beans = new Beans();
		beans.setContext(context);
		
		JAXBContext jc = JAXBContext.newInstance(Beans.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        JAXBElement<Beans> jaxbElement = new JAXBElement<Beans>(new QName("beans"), Beans.class, beans);
        
        String downloadDir = downloadDirectory();
        File xml = new File(downloadDir + entity.getId() + "/conf/context.xml");
        createDirectories(xml);
        marshaller.marshal(jaxbElement, xml);
	}

	private String downloadDirectory() {
		String download = getClass().getResource("/download").getFile();
		int index = download.indexOf("WEB-INF");
		return download.substring(0, index) + "download/";
	}
	
	private Path generateZip(int id) {
		String downloadDir = downloadDirectory();
		
		logger.info("compactando arquivos para download...");
		try {
			File zipDir = new File(downloadDir + id);
			File fzip = new File(downloadDir + id + "_" + AGENT_ZIP);
			zip(zipDir, fzip);
			return fzip.toPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void zip(File directory, File zipfile) throws IOException {
		createDirectories(zipfile);
		
	    URI base = directory.toURI();
	    Deque<File> queue = new LinkedList<File>();
	    queue.push(directory);
	    OutputStream out = new FileOutputStream(zipfile);
	    Closeable res = out;
	    try {
	      ZipOutputStream zout = new ZipOutputStream(out);
	      
	      while (!queue.isEmpty()) {
	        directory = queue.pop();
	        for (File kid : directory.listFiles()) {
	          String name = base.relativize(kid.toURI()).getPath();
	          
	          if (kid.isDirectory()) {
	            queue.push(kid);
	            name = name.endsWith("/") ? name : name + "/";
	            zout.putNextEntry(new ZipEntry(name));
	          } else {
	            zout.putNextEntry(new ZipEntry(name));
	            IOUtils.copy(new FileInputStream(kid), zout);
	            zout.closeEntry();
	          }
	        }
	      }
	      zout.finish();
	      zout.close();
	    } finally {
	    	out.close();
	    	res.close();
	    }
	  }

	private void createDirectories(File file) {
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

//	public void searchRoute(String param, Session session) {
//		for (Route route : contextDao.search(param)) {
//			JsonObject addMessage = createListMessage(route);
//			sendToSession(session, addMessage);
//		}
//		
//		
//	}
	

}
