package com.jhjewel.billing.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class BaseModel implements Serializable {
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Transient
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	private Date updated;

	@Column
	@Enumerated(EnumType.STRING)
	private AccountState state;

	@PrePersist
	protected void onCreate() {
		updated = created = new Date();
		state = AccountState.ACTIVE;
	}

	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
		state = AccountState.ACTIVE;
	}

	public enum AccountState {
		DELETED {
			public String toString() {
				return "DELETED";
			}
		},

		ACTIVE {
			public String toString() {
				return "ACTIVE";
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
