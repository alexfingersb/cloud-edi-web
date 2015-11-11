package br.com.it3.model.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.it3.model.dao.interfaces.MessageLogDAO;
import br.com.it3.model.entities.MessageLog;

@Stateless
public class LoggerManager extends JpaBaseDAO<MessageLog> implements MessageLogDAO {
	Logger logger = Logger.getLogger(LoggerManager.class.getName());

	public LoggerManager() {
	}

	@Override
	public MessageLog update(MessageLog message) {
		em = emf.createEntityManager();

		System.out.println("Update message logger with id " + message.getId());

		MessageLog entity = em.find(MessageLog.class, message.getId());

		entity.setReceivedDate(message.getReceivedDate());
		entity.setRouteTo(message.getRouteTo());

		if (!em.getTransaction().isActive())
			em.getTransaction().begin();

		try {
			em.merge(entity);
			em.getTransaction().commit();
			// em.close();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}
		return entity;
	}

	public MessageLog findByCrc(long crc) {
		// if (em == null)
		em = emf.createEntityManager();

		System.out.println("Find message with crc " + crc);

		Query query = em.createNamedQuery("MessageLog.findByCrc");
		query.setParameter("crc", crc);
		MessageLog message = null;
		try {
			message = (MessageLog) query.getSingleResult();
		} catch (NoResultException ex) {
			System.out
					.println("Message Logger do arquivo nao encontrado. Registrar novo!");
		}
		em.close();
		return message;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getLatestFilesSent(long from) {
		em = emf.createEntityManager();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");  
		sql.append("ml.file_name, ml.file_length, ml.file_date, ml.crc, ml.Sent_Date, ml.received_date, ");
		sql.append("userfrom.Name as sender, userto.name as receiver ");
		sql.append("FROM message_log ml ");
		sql.append("INNER JOIN route_from rfrom    ON rfrom.id 				= ml.route_from_id "); 
		sql.append("LEFT JOIN route_to rto         ON rto.id   				= ml.Route_To_Id ");
		sql.append("INNER JOIN route_uri urifrom   ON urifrom.id 			= rfrom.route_uri_id ");
		sql.append("LEFT JOIN route_uri urito      ON urito.id   			= rto.route_uri_id ");
		sql.append("INNER JOIN user_route urfrom   ON urfrom.route_uri_id 	= urifrom.id ");
		sql.append("LEFT JOIN user_route urto      ON urto.route_uri_id   	= urito.id ");
		sql.append("INNER JOIN \"USER\" userfrom   ON userfrom.id 			= urfrom.user_id ");
		sql.append("LEFT JOIN \"USER\" userto      ON userto.id   			= urto.user_id ");
		sql.append("AND urfrom.user_id = :userId ");
		
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("userId", from);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			list  = (List<Object[]>) query.getResultList();
		} catch (NoResultException ex) {
			System.out.println("Erro ao procurar lista de arquivos enviados! " + ex.getMessage());
		}
		em.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getLatestFilesReceived(long to) {
		em = emf.createEntityManager();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");  
		sql.append("ml.file_name, ml.file_length, ml.file_date, ml.crc, ml.Sent_Date, ml.received_date, ");
		sql.append("userfrom.Name as sender, userto.name as receiver ");
		sql.append("FROM message_log ml ");
		sql.append("INNER JOIN route_from rfrom    ON rfrom.id 				= ml.route_from_id "); 
		sql.append("INNER JOIN route_to rto        ON rto.id   				= ml.Route_To_Id ");
		sql.append("INNER JOIN route_uri urifrom   ON urifrom.id 			= rfrom.route_uri_id ");
		sql.append("INNER JOIN route_uri urito     ON urito.id   			= rto.route_uri_id ");
		sql.append("INNER JOIN user_route urfrom   ON urfrom.route_uri_id 	= urifrom.id ");
		sql.append("INNER JOIN user_route urto     ON urto.route_uri_id   	= urito.id ");
		sql.append("INNER JOIN \"USER\" userfrom   ON userfrom.id 			= urfrom.user_id ");
		sql.append("INNER JOIN \"USER\" userto     ON userto.id   			= urto.user_id ");
		sql.append("AND urto.user_id = :userId ");
		
		
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("userId", to);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			list  = (List<Object[]>) query.getResultList();
		} catch (NoResultException ex) {
			System.out.println("Erro ao procurar lista de arquivos recebidos! " + ex.getMessage());
		}
		em.close();
		return list;
	}

}
