package br.com.it3.model.xml;

import javax.xml.bind.annotation.XmlElement;

public class Choice {
	
	@XmlElement
	private When when;

	public void setWhen(When when) {
		this.when = when;
	}
	
}
