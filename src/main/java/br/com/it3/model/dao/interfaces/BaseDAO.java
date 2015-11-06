package br.com.it3.model.dao.interfaces;

public interface BaseDAO<E> {
	void persist(E entity);

	void remove(E entity);

	E findById(long id);
	
}
