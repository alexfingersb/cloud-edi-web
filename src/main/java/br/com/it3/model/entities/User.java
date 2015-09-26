package br.com.it3.model.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.it3.model.enums.Profile;

@Entity
@NamedQueries({
	@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
	@NamedQuery(name = "User.searchByName", query = "SELECT u FROM User u WHERE u.name LIKE :name"),
	@NamedQuery(name = "User.searchByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
})
@Table(name="\"USER\"")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "USER_ID_GENERATOR", sequenceName = "SEQ_USER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GENERATOR")
	private long id;

	private String name;
	
	private String email;
	
	private String username;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Profile profile;

	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String login) {
		this.username = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
