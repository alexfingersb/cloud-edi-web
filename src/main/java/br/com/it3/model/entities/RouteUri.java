package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ROUTE_URI database table.
 * 
 */
@Entity
@Table(name="ROUTE_URI")
@NamedQuery(name="RouteUri.findAll", query="SELECT r FROM RouteUri r")
public class RouteUri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROUTE_URI_ID_GENERATOR", sequenceName="SEQ_ROUTE_URI_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROUTE_URI_ID_GENERATOR")
	private long id;

	@Column(name="CONTEXT_PATH")
	private String contextPath;

	private String options;

	private String scheme;

	//bi-directional many-to-one association to RouteFrom
	@OneToMany(mappedBy="routeUri")
	private List<RouteFrom> routeFroms;

	//bi-directional many-to-one association to RouteTo
	@OneToMany(mappedBy="routeUri")
	private List<RouteTo> routeTos;

	public RouteUri() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getOptions() {
		return this.options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getScheme() {
		return this.scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public List<RouteFrom> getRouteFroms() {
		return this.routeFroms;
	}

	public void setRouteFroms(List<RouteFrom> routeFroms) {
		this.routeFroms = routeFroms;
	}

	public RouteFrom addRouteFrom(RouteFrom routeFrom) {
		getRouteFroms().add(routeFrom);
		routeFrom.setRouteUri(this);

		return routeFrom;
	}

	public RouteFrom removeRouteFrom(RouteFrom routeFrom) {
		getRouteFroms().remove(routeFrom);
		routeFrom.setRouteUri(null);

		return routeFrom;
	}

	public List<RouteTo> getRouteTos() {
		return this.routeTos;
	}

	public void setRouteTos(List<RouteTo> routeTos) {
		this.routeTos = routeTos;
	}

	public RouteTo addRouteTo(RouteTo routeTo) {
		getRouteTos().add(routeTo);
		routeTo.setRouteUri(this);

		return routeTo;
	}

	public RouteTo removeRouteTo(RouteTo routeTo) {
		getRouteTos().remove(routeTo);
		routeTo.setRouteUri(null);

		return routeTo;
	}

}