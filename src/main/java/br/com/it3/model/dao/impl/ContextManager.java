package br.com.it3.model.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.it3.model.dao.interfaces.ContextDAO;
import br.com.it3.model.entities.Route;
import br.com.it3.model.entities.RouteFrom;
import br.com.it3.model.entities.RouteTo;
import br.com.it3.model.entities.User;

@Stateless
public class ContextManager extends JpaBaseDAO<Route> implements ContextDAO {
	Logger logger = Logger.getLogger(ContextManager.class.getName());
	
	
	public ContextManager() {
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Route> findAll() {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("Route.findAll");
		List<Route> list = (List<Route>) query.getResultList();
		em.close();
		return list;
	}

	@Override
	public Route update(Route route) {
		if (em == null)
			em = emf.createEntityManager();
		
		System.out.println("Update Route ID " + route.getId());
		
		Route entity = em.find(Route.class, route.getId());
		if (!em.getTransaction().isActive()) 
			em.getTransaction().begin();
		try {
			em.merge(entity);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}
		return entity;
	}
	
	public User getUser(long rid, long uid) {
		//if (em == null)
			em = emf.createEntityManager();
		
		Query query = em.createNamedQuery("UserRoute.findUser");
		query.setParameter("rid", rid);
		query.setParameter("uid", uid);
		User user = null;
		try {
			user = (User) query.getSingleResult();
		} catch (NoResultException ex) {
			System.out.println("Usuario nao encontrado");
		}
		em.close();
		return user;
	}


//	@SuppressWarnings("unchecked")
//	public List<Route> search(String param) {
//		if (em == null)
//			em = emf.createEntityManager();
//		
//		Query query = em.createNamedQuery("Route.search");
//		query.setParameter("param", "%" + param + "%");
//		List<Route> list = (List<Route>) query.getResultList();
//		em.close();
//		return list;
//	}
	
	@Override
	public RouteFrom getRouteFrom(long fromId) {
		em = emf.createEntityManager();
		RouteFrom routeFrom = em.find(RouteFrom.class, fromId);
		em.close();
		return routeFrom;
	}

	@Override
	public RouteTo getRouteTo(long toId) {
		em = emf.createEntityManager();
		RouteTo routeTo = em.find(RouteTo.class, toId);
		em.close();
		return routeTo;
	}
	
}
