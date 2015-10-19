package br.com.it3.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class CamelContext {
	
	@XmlAttribute
	private final String xmlns = "http://camel.apache.org/schema/spring";
	
	@XmlElement (name="route")
	private List<Route> route;
	

	public CamelContext() {
		route = new ArrayList<Route>();
	}


	public List<Route> getRoute() {
		return route;
	}


	public void addRoute(Route route) {
		this.route.add(route);
	}

}
