package br.com.it3.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


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
	@OneToMany(mappedBy="routeUri", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<RouteFrom> routeFrom;

	//bi-directional many-to-one association to RouteTo
	@OneToMany(mappedBy="routeUri", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<RouteTo> routeTo;
	
	public RouteUri() {
		routeTo = new ArrayList<RouteTo>();
		routeFrom = new ArrayList<RouteFrom>();
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

	public List<RouteFrom> getRouteFrom() {
		return this.routeFrom;
	}

	public void setRouteFrom(List<RouteFrom> routeFrom) {
		this.routeFrom = routeFrom;
	}

	public RouteFrom addRouteFrom(RouteFrom routeFrom) {
		getRouteFrom().add(routeFrom);
		routeFrom.setRouteUri(this);

		return routeFrom;
	}

	public RouteFrom removeRouteFrom(RouteFrom routeFrom) {
		getRouteFrom().remove(routeFrom);
		routeFrom.setRouteUri(null);

		return routeFrom;
	}

	public List<RouteTo> getRouteTo() {
		return this.routeTo;
	}

	public void setRouteTo(List<RouteTo> routeTo) {
		this.routeTo = routeTo;
	}

	public RouteTo addRouteTo(RouteTo routeTo) {
		getRouteTo().add(routeTo);
		routeTo.setRouteUri(this);

		return routeTo;
	}

	public RouteTo removeRouteTo(RouteTo routeTo) {
		getRouteTo().remove(routeTo);
		routeTo.setRouteUri(null);

		return routeTo;
	}

}