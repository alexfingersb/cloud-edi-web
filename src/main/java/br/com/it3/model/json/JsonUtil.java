package br.com.it3.model.json;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.User;

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
		return provider.createObjectBuilder()
                .add("action", action)
                .add("id", route.getId())
                .add("name", route.getDescription())
                .build();
	}
}
