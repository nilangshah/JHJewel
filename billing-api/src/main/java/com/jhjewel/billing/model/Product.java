package com.jhjewel.billing.model;

public class Product extends BaseModel {

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public double getNetBuyWeight() {
		return netBuyWeight;
	}

	public void setNetBuyWeight(double netBuyWeight) {
		this.netBuyWeight = netBuyWeight;
	}

	public int getKdnCount() {
		return kdnCount;
	}

	public void setKdnCount(int kdnCount) {
		this.kdnCount = kdnCount;
	}

	public int getKdnBuyPrice() {
		return kdnBuyPrice;
	}

	public void setKdnBuyPrice(int kdnBuyPrice) {
		this.kdnBuyPrice = kdnBuyPrice;
	}

	public double getNetSellWeight() {
		return netSellWeight;
	}

	public void setNetSellWeight(double netSellWeight) {
		this.netSellWeight = netSellWeight;
	}

	public int getKdnSellPrice() {
		return kdnSellPrice;
	}

	public void setKdnSellPrice(int kdnSellPrice) {
		this.kdnSellPrice = kdnSellPrice;
	}

	public int getDealerId() {
		return dealerId;
	}

	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public double getPurity() {
		return purity;
	}

	public void setPurity(double purity) {
		this.purity = purity;
	}

	public double getBuyWastage() {
		return buyWastage;
	}

	public void setBuyWastage(double buyWastage) {
		this.buyWastage = buyWastage;
	}

	public double getSellWastage() {
		return sellWastage;
	}

	public void setSellWastage(double sellWastage) {
		this.sellWastage = sellWastage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int productId;
	private String productName;
	private String productCategory;
	private double grossWeight;
	private double netBuyWeight;
	private int kdnCount;
	private int kdnBuyPrice;
	private double netSellWeight;
	private int kdnSellPrice;
	private int dealerId;
	private String picURL;
	private boolean sold;
	private double purity;
	private double buyWastage;
	private double sellWastage;

}
