package br.com.it3.model.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Query;

import br.com.it3.model.dao.interfaces.UserDAO;
import br.com.it3.model.entities.User;

public class UserManager extends JpaBaseDAO<User> implements UserDAO<User> {
	Logger logger = Logger.getLogger(UserManager.class.getName());

	public UserManager() {
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		logger.info("find all users");
		Query query = entityManager.createNamedQuery("User.findAll");
		return (List<User>) query.getResultList();
	}

}
