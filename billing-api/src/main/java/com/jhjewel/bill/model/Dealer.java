package com.jhjewel.bill.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Dealer")
public class Dealer extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int dealerId;
	@Column(nullable = false)
	private String dealerName;
	@Column(nullable = false)
	private String companyName;
	private String address;
	private String panNo;
	private String mobileNo;
	private String vatTin;
	@Column(nullable = false)
	private String dealerType;

	public Dealer() {
		super();
		dealerType="buyer";
	}

	public Dealer(int dealerId, String dealerName, String companyName, String address, String panNo, String mobileNo,
			String vatTin, String dealerType) {
		super();
		this.dealerId = dealerId;
		this.dealerName = dealerName;
		this.companyName = companyName;
		this.address = address;
		this.panNo = panNo;
		this.mobileNo = mobileNo;
		this.vatTin = vatTin;
		this.dealerType = dealerType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Dealer [dealerId=" + dealerId + ", dealerName=" + dealerName + ", companyName=" + companyName
				+ ", address=" + address + ", panNo=" + panNo + ", mobileNo=" + mobileNo + ", vatTin=" + vatTin
				+ ", dealerType=" + dealerType + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + dealerId;
		result = prime * result + ((dealerName == null) ? 0 : dealerName.hashCode());
		result = prime * result + ((dealerType == null) ? 0 : dealerType.hashCode());
		result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
		result = prime * result + ((panNo == null) ? 0 : panNo.hashCode());
		result = prime * result + ((vatTin == null) ? 0 : vatTin.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dealer other = (Dealer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (dealerId != other.dealerId)
			return false;
		if (dealerName == null) {
			if (other.dealerName != null)
				return false;
		} else if (!dealerName.equals(other.dealerName))
			return false;
		if (dealerType == null) {
			if (other.dealerType != null)
				return false;
		} else if (!dealerType.equals(other.dealerType))
			return false;
		if (mobileNo == null) {
			if (other.mobileNo != null)
				return false;
		} else if (!mobileNo.equals(other.mobileNo))
			return false;
		if (panNo == null) {
			if (other.panNo != null)
				return false;
		} else if (!panNo.equals(other.panNo))
			return false;
		if (vatTin == null) {
			if (other.vatTin != null)
				return false;
		} else if (!vatTin.equals(other.vatTin))
			return false;
		return true;
	}

	/**
	 * @return the dealerId
	 */
	public int getDealerId() {
		return dealerId;
	}

	/**
	 * @param dealerId
	 *            the dealerId to set
	 */
	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	/**
	 * @return the dealerName
	 */
	public String getDealerName() {
		return dealerName;
	}

	/**
	 * @param dealerName
	 *            the dealerName to set
	 */
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the panNo
	 */
	public String getPanNo() {
		return panNo;
	}

	/**
	 * @param panNo
	 *            the panNo to set
	 */
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	/**
	 * @return the mobileNo
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param mobileNo
	 *            the mobileNo to set
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * @return the vatTin
	 */
	public String getVatTin() {
		return vatTin;
	}

	/**
	 * @param vatTin
	 *            the vatTin to set
	 */
	public void setVatTin(String vatTin) {
		this.vatTin = vatTin;
	}

	/**
	 * @return the dealerType
	 */
	public String getDealerType() {
		return dealerType;
	}

	/**
	 * @param dealerType
	 *            the dealerType to set
	 */
	public void setDealerType(String dealerType) {
		this.dealerType = dealerType;
	}

}
