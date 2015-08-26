package br.com.it3.model.dao.impl;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import br.com.it3.model.dao.interfaces.BaseDAO;

abstract class JpaBaseDAO<E>  implements BaseDAO<E> {
	private Logger logger = Logger.getLogger(JpaBaseDAO.class);

	@PersistenceUnit (unitName = "cloud.edi.web")
	protected EntityManagerFactory emf;

	protected EntityManager em;

	protected Class entityClass;
	
	public JpaBaseDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		System.out.println("type argument: " + genericSuperclass.getActualTypeArguments()[0]);
		this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[0];
		this.emf = Persistence.createEntityManagerFactory("cloud.edi.web");
	}

	@Override
	public void persist(E entity) {
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} 
	}

	@Override
	public void remove(E entity) {
		logger.info(String.format("Remove entity %s from database",entity.toString()));
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.contains(entity) ? entity : em.merge(entity));
		em.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E findById(long id) {
		em = emf.createEntityManager();
		return (E) em.find(entityClass,id);
	}

}
