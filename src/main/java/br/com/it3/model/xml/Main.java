package br.com.it3.model.xml;

import static br.com.it3.model.xml.XML_DATA.BEANS;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

public class Main {

	public static void main(String[] args) {
		
		try {
			createXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static void createXml() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Beans.class);
		 
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        JAXBElement<Beans> jaxbElement = new JAXBElement<Beans>(new QName("beans"), Beans.class, BEANS);
        marshaller.marshal(jaxbElement, System.out);
	}

}
