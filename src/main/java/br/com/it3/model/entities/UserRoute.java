package br.com.it3.model.entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the USER_ROUTE database table.
 * 
 */
@Entity
@Table(name="USER_ROUTE")
@NamedQueries({
		@NamedQuery(name="UserRoute.findAll", query="SELECT u FROM UserRoute u"),
		@NamedQuery(name="UserRoute.findUser", query="SELECT DISTINCT u "
				+ "FROM User u, UserRoute ur, Route r, RouteUri ru "
				+ "JOIN ur.user urUser "
				+ "JOIN ur.route urRoute "
				+ "JOIN ur.routeUri urUri "
				+ "WHERE urUser.id = u.id "
				+ "	 AND urRoute.id = r.id "
				+ "  AND urUri.id = ru.id "
				+ "  AND ur.route.id = :rid "
				+ "  AND ur.routeUri.id = :uid ") 
})
public class UserRoute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_ROUTE_ID_GENERATOR", sequenceName="SEQ_USER_ROUTE_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ROUTE_ID_GENERATOR")
	private long id;

	@OneToOne (cascade = CascadeType.MERGE)
	@JoinColumn (name = "USER_ID")
	private User user;

	//bi-directional many-to-one association to Route
	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="ROUTE_ID")
	private Route route;
	
	@OneToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="ROUTE_URI_ID")
	private RouteUri routeUri;

	public UserRoute() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Route getRoute() {
		return this.route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public RouteUri getRouteUri() {
		return routeUri;
	}

	public void setRouteUri(RouteUri routeUri) {
		this.routeUri = routeUri;
	}

}