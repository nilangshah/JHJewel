package com.jhjewel.bill.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@MappedSuperclass
@SQLDelete(sql = "UPDATE account SET state = 'DELETED' WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "state <> 'DELETED'")
public abstract class BaseModel {
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
	}

	@PreRemove
	public void deleteUser() {
		this.state = AccountState.DELETED;
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
}
