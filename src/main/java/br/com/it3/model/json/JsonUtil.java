package br.com.it3.model.json;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import br.com.it3.model.dao.impl.ContextManager;
import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.RouteFrom;
import br.com.it3.model.entities.RouteTo;
import br.com.it3.model.entities.RouteUri;
import br.com.it3.model.entities.User;
import br.com.it3.model.entities.UserRoute;

public class JsonUtil {

	JsonProvider provider = JsonProvider.provider();

	public JsonUtil() {
	}
	
	public JsonObject buildUserJson(User user, String action) {
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
	
	public JsonObject buildRouteJson(Route route, String action) {
		
		//Rota
		System.out.println("Rota: " + route.getDescription());
		
		//Remetente
		RouteFrom routeFrom = route.getRouteFrom();
		RouteUri fromUri = routeFrom.getRouteUri();
		
		User userFrom = null;
		ContextManager dao = new ContextManager();
		if ("edit".equals(action) || "list".equals(action)) {
			userFrom = dao.getUser(route.getId(), fromUri.getId());
		}
		
		//Destinatarios
		User userTo = null;
		JsonArrayBuilder ja = Json.createArrayBuilder();
		for (RouteTo routeTo : routeFrom.getRouteTo()) {
			RouteUri toUri = routeTo.getRouteUri();
			userTo = dao.getUser(route.getId(), toUri.getId());
			
			ja.add(provider.createObjectBuilder()
					.add("user", (userTo != null ? userTo.getName() : "unknow"))
					.add("protocol", toUri.getScheme())
					.add("filter", routeTo.getChoiceWhen())
					.add("cpath", toUri.getContextPath())
					.add("options", (toUri.getOptions() != null ? toUri.getOptions() : ""))
					.build());
		}
		
		JsonObject context = provider.createObjectBuilder()
			.add("route", ja).build();
		
		
		return provider.createObjectBuilder()
                .add("action", action)
                .add("id", route.getId())
                .add("description", route.getDescription())
                .add("user", (userFrom != null ? userFrom.getName() : "unknow"))
                .add("protocol", fromUri.getScheme())
                .add("cpath", fromUri.getContextPath())
                .add("options", (fromUri.getOptions() != null ? fromUri.getOptions() : ""))
                .add("context", context)
                .build();
	}
	
	public JsonObject buildDownloadJson(int id, String url, String action) {
		return provider.createObjectBuilder()
				.add("action", action)
				.add("url", url)
				.add("id", String.valueOf(id))
				.build();
	}
}
