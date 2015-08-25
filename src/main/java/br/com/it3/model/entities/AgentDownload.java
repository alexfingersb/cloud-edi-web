package br.com.it3.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the AGENT_DOWNLOAD database table.
 * 
 */
@Entity
@Table(name="AGENT_DOWNLOAD")
@NamedQuery(name="AgentDownload.findAll", query="SELECT a FROM AgentDownload a")
public class AgentDownload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="AGENT_DOWNLOAD_ID_GENERATOR", sequenceName="SEQ_AGENT_DOWNLOAD_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AGENT_DOWNLOAD_ID_GENERATOR")
	private long id;

	@Column(name="HOST_ADDRESS")
	private String hostAddress;

	@Column(name="USER_ID")
	private String userId;

	public AgentDownload() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHostAddress() {
		return this.hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}