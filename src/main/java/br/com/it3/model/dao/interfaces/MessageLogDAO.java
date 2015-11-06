package br.com.it3.model.dao.interfaces;

import br.com.it3.model.entities.MessageLog;

public interface MessageLogDAO extends BaseDAO<MessageLog> {
	
	MessageLog update(MessageLog message);

}
