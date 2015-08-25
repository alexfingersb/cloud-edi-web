package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ROUTE_TO database table.
 * 
 */
@Entity
@Table(name="ROUTE_TO")
@NamedQuery(name="RouteTo.findAll", query="SELECT r FROM RouteTo r")
public class RouteTo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROUTE_TO_ID_GENERATOR", sequenceName="SEQ_ROUTE_TO_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROUTE_TO_ID_GENERATOR")
	private long id;

	@Column(name="CHOICE_WHEN")
	private String choiceWhen;

	//bi-directional many-to-one association to MessageLog
	@OneToMany(mappedBy="routeTo")
	private List<MessageLog> messageLogs;

	//bi-directional many-to-one association to Route
	@ManyToOne
	private Route route;

	//bi-directional many-to-one association to RouteUri
	@ManyToOne
	@JoinColumn(name="ROUTE_URI_ID")
	private RouteUri routeUri;

	public RouteTo() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChoiceWhen() {
		return this.choiceWhen;
	}

	public void setChoiceWhen(String choiceWhen) {
		this.choiceWhen = choiceWhen;
	}

	public List<MessageLog> getMessageLogs() {
		return this.messageLogs;
	}

	public void setMessageLogs(List<MessageLog> messageLogs) {
		this.messageLogs = messageLogs;
	}

	public MessageLog addMessageLog(MessageLog messageLog) {
		getMessageLogs().add(messageLog);
		messageLog.setRouteTo(this);

		return messageLog;
	}

	public MessageLog removeMessageLog(MessageLog messageLog) {
		getMessageLogs().remove(messageLog);
		messageLog.setRouteTo(null);

		return messageLog;
	}

	public Route getRoute() {
		return this.route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public RouteUri getRouteUri() {
		return this.routeUri;
	}

	public void setRouteUri(RouteUri routeUri) {
		this.routeUri = routeUri;
	}

}