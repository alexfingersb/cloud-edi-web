package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ROUTE_FROM database table.
 * 
 */
@Entity
@Table(name="ROUTE_FROM")
@NamedQuery(name="RouteFrom.findAll", query="SELECT r FROM RouteFrom r")
public class RouteFrom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROUTE_FROM_ID_GENERATOR", sequenceName="SEQ_ROUTE_FROM_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROUTE_FROM_ID_GENERATOR")
	private long id;

	@Column(name="\"FROM\"")
	private BigDecimal from;

	//bi-directional many-to-one association to MessageLog
	@OneToMany(mappedBy="routeFrom")
	private List<MessageLog> messageLogs;

	//bi-directional many-to-one association to Route
	@ManyToOne
	private Route route;

	//bi-directional many-to-one association to RouteUri
	@ManyToOne
	@JoinColumn(name="ROUTE_URI_ID")
	private RouteUri routeUri;

	public RouteFrom() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getFrom() {
		return this.from;
	}

	public void setFrom(BigDecimal from) {
		this.from = from;
	}

	public List<MessageLog> getMessageLogs() {
		return this.messageLogs;
	}

	public void setMessageLogs(List<MessageLog> messageLogs) {
		this.messageLogs = messageLogs;
	}

	public MessageLog addMessageLog(MessageLog messageLog) {
		getMessageLogs().add(messageLog);
		messageLog.setRouteFrom(this);

		return messageLog;
	}

	public MessageLog removeMessageLog(MessageLog messageLog) {
		getMessageLogs().remove(messageLog);
		messageLog.setRouteFrom(null);

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