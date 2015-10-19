package br.com.it3.model.xml;

public class XML_DATA {
	
	public static Beans BEANS;
	private static final boolean usechoice = true;
	 
    static {
        BEANS = new Beans();
        
        Address from = new Address();
        from.setUri("file:data/outbox");

        Address to = new Address();
        to.setUri("jms:finger@jms-inbox");

        Route route = new Route();
        route.setId("finger@file-inbox");
        route.setFrom(from);
        
        if (usechoice) {
        	Choice choice = new Choice();
        	When when = new When();
        	when.setTo(to);
        	when.setSimple("ftp");
        	choice.setWhen(when);
        	route.setChoice(choice);
        } else {
        	route.setTo(to);
        }
        
        
        CamelContext context = new CamelContext();
        context.addRoute(route);
        
        BEANS.setContext(context);
    }

}
