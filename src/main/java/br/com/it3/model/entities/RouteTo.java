package br.com.it3.model.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


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

	//bi-directional many-to-one association to RouteFrom
	@ManyToOne (cascade = CascadeType.ALL)
	@JoinColumn(name="ROUTE_FROM_ID")
	private RouteFrom routeFrom;

	//bi-directional many-to-one association to RouteUri
	@ManyToOne (cascade = CascadeType.ALL)
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

	public RouteFrom getRouteFrom() {
		return this.routeFrom;
	}

	public void setRouteFrom(RouteFrom routeFrom) {
		this.routeFrom = routeFrom;
	}

	public RouteUri getRouteUri() {
		return this.routeUri;
	}

	public void setRouteUri(RouteUri routeUri) {
		this.routeUri = routeUri;
	}

}