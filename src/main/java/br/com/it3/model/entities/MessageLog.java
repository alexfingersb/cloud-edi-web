package br.com.it3.model.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the MESSAGE_LOG database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_LOG")
@NamedQueries ({
	@NamedQuery(name = "MessageLog.findAll", query = "SELECT m FROM MessageLog m"),
	@NamedQuery(name = "MessageLog.findByCrc", query = "SELECT m FROM MessageLog m WHERE m.crc = :crc")
})

public class MessageLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MESSAGE_LOG_ID_GENERATOR", sequenceName = "SEQ_MESSAGE_LOG_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_LOG_ID_GENERATOR")
	private long id;

	@Column(name = "FILE_DATE")
	private Timestamp fileDate;

	@Column(name = "FILE_LENGTH")
	private Long fileLength;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "RECEIVED_DATE")
	private Timestamp receivedDate;

	@Column(name = "SENT_DATE")
	private Timestamp sentDate;

	@OneToOne  (cascade = CascadeType.DETACH, optional = true, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ROUTE_FROM_ID", nullable = true, insertable = true, updatable = true)
	private RouteFrom routeFrom;

	@OneToOne  (cascade = CascadeType.DETACH, optional = true, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ROUTE_TO_ID", nullable = true, insertable = true, updatable = true)
	private RouteTo routeTo;

	@Column(name = "CRC")
	private long crc;

	public MessageLog() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getFileDate() {
		return this.fileDate;
	}

	public void setFileDate(Timestamp fileDate) {
		this.fileDate = fileDate;
	}

	public Long getFileLength() {
		return this.fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Timestamp getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Timestamp getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(Timestamp sentDate) {
		this.sentDate = sentDate;
	}

	public RouteFrom getRouteFrom() {
		return this.routeFrom;
	}

	public void setRouteFrom(RouteFrom routeFrom) {
		this.routeFrom = routeFrom;
	}

	public RouteTo getRouteTo() {
		return this.routeTo;
	}

	public void setRouteTo(RouteTo routeTo) {
		this.routeTo = routeTo;
	}

	public long getCrc() {
		return crc;
	}

	public void setCrc(long crc) {
		this.crc = crc;
	}

}