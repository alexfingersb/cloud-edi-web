package br.com.it3.model.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.it3.model.dao.interfaces.UserDAO;
import br.com.it3.model.entities.User;

@Stateless
public class UserManager extends JpaBaseDAO<User> implements UserDAO<User> {
	Logger logger = Logger.getLogger(UserManager.class.getName());
	
	
	public UserManager() {
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("User.findAll");
		List<User> users = (List<User>) query.getResultList();
		em.close();
		return users;
	}

	@Override
	public User update(User user) {
		if (em == null)
			em = emf.createEntityManager();
		
		User entity = em.find(User.class, user.getId());
		entity.setId(user.getId());
		entity.setName(user.getName());
		entity.setEmail(user.getEmail());
		entity.setStatus(user.getStatus());
		entity.setProfile(user.getProfile());
		entity.setUsername(user.getUsername());
		
		em.getTransaction().begin();
		try {
			logger.info("Update user " + entity.getName());
			em.merge(entity);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> search(final String name) {
		if (em == null)
			em = emf.createEntityManager();
		logger.info(String.format("search user by name %s", name));
		Query query = em.createNamedQuery("User.searchByName");
		query.setParameter("name", name + "%");
		List<User> users = (List<User>)query.getResultList();
		em.close();
		return users;
	}

	public User getUser(final String username) {
		if (em == null)
			em = emf.createEntityManager();
		logger.info(String.format("search user by username %s", username));
		Query query = em.createNamedQuery("User.searchByUsername");
		query.setParameter("username", username);
		User user = (User) query.getSingleResult();
		em.close();
		return user;
	}
	
	
}
