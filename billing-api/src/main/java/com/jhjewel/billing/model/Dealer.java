package com.jhjewel.billing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "Dealer")
@SQLDelete(sql = "UPDATE dealer SET state = 'DELETED' WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "state <> 'DELETED'")
public class Dealer extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		dealerType = "buyer";
	}

	public Dealer(String dealerName, String companyName, String address, String panNo, String mobileNo, String vatTin, String dealerType) {
		super();
		this.dealerName = dealerName;
		this.companyName = companyName;
		this.address = address;
		this.panNo = panNo;
		this.mobileNo = mobileNo;
		this.vatTin = vatTin;
		this.dealerType = dealerType;
	}

	@Override
	public String toString() {
		return "Dealer [ dealerName=" + dealerName + ", companyName=" + companyName + ", address=" + address + ", panNo=" + panNo + ", mobileNo="
				+ mobileNo + ", vatTin=" + vatTin + ", dealerType=" + dealerType + "]";
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
