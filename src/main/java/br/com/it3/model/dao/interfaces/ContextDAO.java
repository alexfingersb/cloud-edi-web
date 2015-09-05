package br.com.it3.model.dao.interfaces;

import java.util.List;

import br.com.it3.model.entities.Route;

public interface ContextDAO extends BaseDAO<Route> {
	public List<Route> findAll();

	public Route update(Route entity);


}
