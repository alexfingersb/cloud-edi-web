package br.com.it3.model.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.it3.model.dao.interfaces.ContextDAO;
import br.com.it3.model.entities.Route;
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
		List<Route> users = (List<Route>) query.getResultList();
		em.close();
		return users;
	}

	@Override
	public Route update(Route route) {
		if (em == null)
			em = emf.createEntityManager();
		
		Route entity = em.find(Route.class, route.getId());
		em.getTransaction().begin();
		try {
			em.merge(entity);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		return entity;
	}
	
	public User getUser(long rid, long uid) {
		if (em == null)
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
		//em.close();
		return user;
	}
	
}
