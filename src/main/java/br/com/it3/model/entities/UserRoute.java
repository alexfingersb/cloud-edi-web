package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the USER_ROUTE database table.
 * 
 */
@Entity
@Table(name="USER_ROUTE")
@NamedQuery(name="UserRoute.findAll", query="SELECT u FROM UserRoute u")
public class UserRoute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_ROUTE_ID_GENERATOR", sequenceName="SEQ_USER_ROUTE_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ROUTE_ID_GENERATOR")
	private long id;

	@Column(name="USER_ID")
	private long userId;

	//bi-directional many-to-one association to Route
	@ManyToOne
	private Route route;

	public UserRoute() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Route getRoute() {
		return this.route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}