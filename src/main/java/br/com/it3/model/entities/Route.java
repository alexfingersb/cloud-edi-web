package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ROUTE database table.
 * 
 */
@Entity
@NamedQuery(name="Route.findAll", query="SELECT r FROM Route r")
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROUTE_ID_GENERATOR", sequenceName="SEQ_ROUTE_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROUTE_ID_GENERATOR")
	private long id;

	private String description;

	//bi-directional many-to-one association to RouteFrom
	@OneToMany(mappedBy="route")
	private List<RouteFrom> routeFroms;

	//bi-directional many-to-one association to RouteTo
	@OneToMany(mappedBy="route")
	private List<RouteTo> routeTos;

	//bi-directional many-to-one association to UserRoute
	@OneToMany(mappedBy="route")
	private List<UserRoute> userRoutes;

	public Route() {
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

	public List<RouteFrom> getRouteFroms() {
		return this.routeFroms;
	}

	public void setRouteFroms(List<RouteFrom> routeFroms) {
		this.routeFroms = routeFroms;
	}

	public RouteFrom addRouteFrom(RouteFrom routeFrom) {
		getRouteFroms().add(routeFrom);
		routeFrom.setRoute(this);

		return routeFrom;
	}

	public RouteFrom removeRouteFrom(RouteFrom routeFrom) {
		getRouteFroms().remove(routeFrom);
		routeFrom.setRoute(null);

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
		routeTo.setRoute(this);

		return routeTo;
	}

	public RouteTo removeRouteTo(RouteTo routeTo) {
		getRouteTos().remove(routeTo);
		routeTo.setRoute(null);

		return routeTo;
	}

	public List<UserRoute> getUserRoutes() {
		return this.userRoutes;
	}

	public void setUserRoutes(List<UserRoute> userRoutes) {
		this.userRoutes = userRoutes;
	}

	public UserRoute addUserRoute(UserRoute userRoute) {
		getUserRoutes().add(userRoute);
		userRoute.setRoute(this);

		return userRoute;
	}

	public UserRoute removeUserRoute(UserRoute userRoute) {
		getUserRoutes().remove(userRoute);
		userRoute.setRoute(null);

		return userRoute;
	}

}