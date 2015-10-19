package br.com.it3.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Property {

	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String value;
	
	@XmlElement
	private Bean bean;
	
	public Property () {
	}
	
	public void init() {
		this.bean = new Bean();
		this.bean.setClazz("org.apache.activemq.ActiveMQConnectionFactory");
		this.bean.setId("connectionFactory");
		
		Property property = new Property();
		property.setName("brokerURL");
		property.setValue("tcp://${tcp.host}:${tcp.port}");
		
		this.bean.setProperty(property);
	}
	
	private void setValue(String value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
