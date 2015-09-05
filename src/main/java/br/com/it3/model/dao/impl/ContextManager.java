package br.com.it3.model.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.it3.model.dao.interfaces.ContextDAO;
import br.com.it3.model.entities.Route;

@Stateless
public class ContextManager extends JpaBaseDAO<Route> implements ContextDAO {
	Logger logger = Logger.getLogger(ContextManager.class.getName());
	
	
	public ContextManager() {
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Route> findAll() {
		em = emf.createEntityManager();
		logger.info("find all routes");
		Query query = em.createNamedQuery("Route.findAll");
		List<Route> users = (List<Route>) query.getResultList();
		em.close();
		return users;
	}

	@Override
	public Route update(Route route) {
		if (em == null)
			em = emf.createEntityManager();
		
		Route entity = em.find(Route.class, route.getId());
//		entity.setId(user.getId());
//		entity.setName(user.getName());
//		entity.setEmail(user.getEmail());
//		entity.setStatus(user.getStatus());
//		entity.setProfile(user.getProfile());
//		entity.setUsername(user.getUsername());
		
		em.getTransaction().begin();
		try {
			logger.info("Update route " + entity.getDescription());
			em.merge(entity);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		return entity;
	}
}
