package com.jhjewel.bill.model;

import java.io.Serializable;

public class Product extends BaseModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int productId;
	private String productName;
	private String productCategory;
	private double grossWeight;
	private double netBuyWeight;
	private int kdnCount;
	private	int kdnBuyPrice;
	private double netSellWeight;
	private int kdnSellPrice;
	private int dealerId;
	private String picURL;
	private boolean sold;
	private double purity;
	private double buyWastage;
	private double sellWastage;
	
	
}
