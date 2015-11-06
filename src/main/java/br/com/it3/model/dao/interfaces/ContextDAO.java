package br.com.it3.model.dao.interfaces;

import java.util.List;

import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.RouteFrom;
import br.com.it3.model.entities.RouteTo;

public interface ContextDAO extends BaseDAO<Route> {
	public List<Route> findAll();

	public Route update(Route entity);
	
	public RouteFrom getRouteFrom(long id);
	
	public RouteTo getRouteTo(long id);


}
