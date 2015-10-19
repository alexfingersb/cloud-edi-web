package br.com.it3.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Beans {
	
	@XmlAttribute (name="xmlns")
	private final String xmlns = "http://www.springframework.org/schema/beans";
	
	@XmlAttribute (name="xmlns:xsi")
	private final String xsi = "http://www.w3.org/2001/XMLSchema-instance"; 
	
	@XmlAttribute (name="xmlns:context")
	private final String context = "http://www.springframework.org/schema/context";
	
	@XmlAttribute (name="xmlns:camel")
	private final String camel = "http://camel.apache.org/schema/spring";
	
	@XmlAttribute (name="xsi:schemaLocation")
	private final String schemaLocation =
		         "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"
		         + " http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd"
		         + " http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring.xsd";
	
	@XmlElement (name = "context:property-placeholder")
	private Location location;
	
	@XmlElement (name = "bean")
	private List<Bean> bean;
	
	@XmlElement(name="camelContext")
    private CamelContext camelContext;
	
	

	public Beans() {
		this.setLocation(new Location());
		this.bean = new ArrayList<Bean>();
		
		Property property = new Property();
		property.init();
		property.setName("connectionFactory");
		Bean factory = new Bean();
		factory.setId("jms");
		factory.setClazz("org.apache.activemq.camel.component.ActiveMQComponent");
		factory.setProperty(property);
		this.bean.add(factory);
		
		Bean processLogger = new Bean();
		processLogger.setId("processLogger");
		processLogger.setClazz("br.com.it3.logger.ProcessLogger");
		this.bean.add(processLogger);
	}
	
	public void setContext(CamelContext context) {
		this.camelContext = context;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
