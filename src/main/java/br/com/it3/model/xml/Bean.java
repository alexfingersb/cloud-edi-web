package br.com.it3.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Bean {

	@XmlAttribute
	private String id;
	
	@XmlAttribute (name = "class")
	private String clazz;
	
	@XmlElement
	private Property property;
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setProperty(Property property) {
		this.property = property;
	}
	
}
