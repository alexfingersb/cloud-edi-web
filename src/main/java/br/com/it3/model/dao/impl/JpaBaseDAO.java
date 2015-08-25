package br.com.it3.model.dao.impl;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.it3.model.dao.interfaces.BaseDAO;

abstract class JpaBaseDAO<E> implements BaseDAO<E> {

	@PersistenceContext
	protected EntityManager entityManager;
	protected Class entityClass;
	
	public JpaBaseDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		System.out.println("type argument: " + genericSuperclass.getActualTypeArguments()[0]);
		this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	public void persist(E entity) {
		entityManager.persist(entity);
	}

	@Override
	public void remove(E entity) {
		entityManager.remove(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E findById(Integer id) {
		return (E) entityManager.find(entityClass,id);
	}

}
