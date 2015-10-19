package br.com.it3.model.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Address {
	
	@XmlAttribute
	private String uri;

	public void setUri(String uri) {
		this.uri = uri;
	}

}
