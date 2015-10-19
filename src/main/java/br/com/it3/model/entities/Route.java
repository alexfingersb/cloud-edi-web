package br.com.it3.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the ROUTE database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Route.findAll", query = "SELECT r FROM Route r"),
	@NamedQuery(name = "Route.listAll", query = "select r.id, r.description, ru.scheme, ru.contextPath, ru.options"
			+ " FROM Route r"
			+ " JOIN r.routeFrom rf "
			+ " JOIN rf.routeUri ru "),
	@NamedQuery(name = "Route.search", query = "SELECT r FROM Route r WHERE r.description LIKE :param OR r.id LIKE :param")
})
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ROUTE_ID_GENERATOR", sequenceName = "SEQ_ROUTE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROUTE_ID_GENERATOR")
	private long id;

	private String description;

	// bi-directional many-to-one association to RouteFrom
	@OneToOne(mappedBy = "route", cascade = CascadeType.ALL)
	private RouteFrom routeFrom;

	// bi-directional many-to-one association to RouteFrom
	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserRoute> userRoute;

	public Route() {
		userRoute = new ArrayList<UserRoute>();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RouteFrom getRouteFrom() {
		return this.routeFrom;
	}

	public void setRouteFrom(RouteFrom routeFrom) {
		this.routeFrom = routeFrom;
	}

	public RouteFrom addRouteFrom(RouteFrom routeFrom) {
		// getRouteFrom().add(routeFrom);
		routeFrom.setRoute(this);

		return routeFrom;
	}

	public RouteFrom removeRouteFrom(RouteFrom routeFrom) {
		// getRouteFrom().remove(routeFrom);
		routeFrom.setRoute(null);

		return routeFrom;
	}

	public List<UserRoute> getUserRoute() {
		return userRoute;
	}

	public void addUserRoute(UserRoute userRoute) {
		this.userRoute.add(userRoute);
		userRoute.setRoute(this);
	}

}