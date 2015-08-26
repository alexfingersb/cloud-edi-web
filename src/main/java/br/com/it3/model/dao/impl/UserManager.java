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
		this.em = emf.createEntityManager();
		logger.info("find all users");
		Query query = em.createNamedQuery("User.findAll");
		List<User> users = (List<User>) query.getResultList();
		em.close();
		return users;
	}
}
