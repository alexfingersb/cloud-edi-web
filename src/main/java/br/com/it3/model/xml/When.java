package br.com.it3.model.xml;

import javax.xml.bind.annotation.XmlElement;

public class When {
	
	@XmlElement
	public String simple = "${header.CamelFileName} regex ";
	
	@XmlElement
	private Address to;

	
	public void setTo(Address to) {
		this.to = to;
	}
	
	public void setSimple(String ext) {
		this.simple += "'^.*" + ext + "$'";
	}

}
