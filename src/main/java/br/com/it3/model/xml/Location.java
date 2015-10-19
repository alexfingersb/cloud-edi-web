package br.com.it3.model.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {
	
	@XmlAttribute (name = "location")
	private String location = "classpath:camel.properties";
	
	@XmlAttribute (name = "ignore-resource-not-found")
	private String ignore = "true";

}
