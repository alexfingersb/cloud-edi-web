package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the MESSAGE_LOG database table.
 * 
 */
@Entity
@Table(name="MESSAGE_LOG")
@NamedQuery(name="MessageLog.findAll", query="SELECT m FROM MessageLog m")
public class MessageLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MESSAGE_LOG_ID_GENERATOR", sequenceName="SEQ_MESSAGE_LOG_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MESSAGE_LOG_ID_GENERATOR")
	private long id;

	@Column(name="FILE_DATE")
	private Timestamp fileDate;

	@Column(name="FILE_LENGTH")
	private BigDecimal fileLength;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="RECEIVED_DATE")
	private Timestamp receivedDate;

	@Column(name="SENT_DATE")
	private Timestamp sentDate;

	//bi-directional many-to-one association to RouteFrom
	@ManyToOne
	@JoinColumn(name="ROUTE_FROM_ID")
	private RouteFrom routeFrom;

	//bi-directional many-to-one association to RouteTo
	@ManyToOne
	@JoinColumn(name="ROUTE_TO_ID")
	private RouteTo routeTo;

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

	public BigDecimal getFileLength() {
		return this.fileLength;
	}

	public void setFileLength(BigDecimal fileLength) {
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

}