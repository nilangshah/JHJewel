package com.jhjewel.billing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET state = 'DELETED' WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "state <> 'DELETED'")
public class User extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@Column(nullable = false)
	@JsonIgnore
	private String passwordSalt;

	public User() {

	}

	public User(String username, String password, String passwordSalt) {
		super();
		this.username = username;
		this.password = password;
		this.passwordSalt = passwordSalt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [username=" + username + "]";
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passwordSalt
	 */
	public String getPasswordSalt() {
		return passwordSalt;
	}

	/**
	 * @param passwordSalt
	 *            the passwordSalt to set
	 */
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

}
