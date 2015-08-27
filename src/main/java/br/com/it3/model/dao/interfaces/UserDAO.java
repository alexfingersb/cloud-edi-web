package br.com.it3.model.dao.interfaces;

import java.util.List;

public interface UserDAO<User> extends BaseDAO<User> {
	public List<User> findAll();
	
	public User update(User entity);
	
}
