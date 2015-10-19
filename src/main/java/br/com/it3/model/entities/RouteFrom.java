package br.com.it3.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


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

	//bi-directional many-to-one association to Route
	@OneToOne (cascade = CascadeType.ALL)
	private Route route;

	//bi-directional many-to-one association to RouteTo
	@OneToMany(mappedBy="routeFrom", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<RouteTo> routeTo;

	//bi-directional many-to-one association to RouteUri
	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="ROUTE_URI_ID")
	private RouteUri routeUri;

	public RouteFrom() {
		routeTo = new ArrayList<RouteTo>();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Route getRoute() {
		return this.route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public List<RouteTo> getRouteTo() {
		return this.routeTo;
	}

	public void setRouteTo(List<RouteTo> routeTo) {
		this.routeTo = routeTo;
	}

	public RouteTo addRouteTo(RouteTo routeTo) {
		getRouteTo().add(routeTo);
		routeTo.setRouteFrom(this);

		return routeTo;
	}

	public RouteTo removeRouteTo(RouteTo routeTo) {
		getRouteTo().remove(routeTo);
		routeTo.setRouteFrom(null);

		return routeTo;
	}

	public RouteUri getRouteUri() {
		return this.routeUri;
	}

	public void setRouteUri(RouteUri routeUri) {
		this.routeUri = routeUri;
	}
}