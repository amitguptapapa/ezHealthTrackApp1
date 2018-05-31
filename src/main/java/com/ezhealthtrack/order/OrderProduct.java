package com.ezhealthtrack.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProduct {

	@SerializedName("order_product_id")
	@Expose
	private String orderProductId;
	@SerializedName("order_id")
	@Expose
	private String orderId;
	@SerializedName("product_id")
	@Expose
	private String productId;
	@Expose
	private String name;
	@Expose
	private Object model;
	@Expose
	private String quantity;
	@Expose
	private String price;
	@Expose
	private String total;
	@Expose
	private Object tax;
	@Expose
	private Object reward;
	@Expose
	private String status;
	@Expose
	private String type;
	@SerializedName("product_model")
	@Expose
	private String productModel;
	@SerializedName("homeCounsultationCharges")
	@Expose
	private String homeCounsultationCharges;

	public LabTest labTest;
	public LabPanel labPanel;
	//public LabCharges labCharges;

	public static final String STATUS_ENTER_REPORT = "0";
	public static final String STATUS_REPORT_DONE = "1";
	public static final String STATUS_ERROR_REPORT = "2";

	/**
	 * 
	 * @return The orderProductId
	 */
	public String getOrderProductId() {
		return orderProductId;
	}

	/**
	 * 
	 * @param orderProductId
	 *            The order_product_id
	 */
	public void setOrderProductId(String orderProductId) {
		this.orderProductId = orderProductId;
	}

	/**
	 * y*
	 * 
	 * @return The orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 
	 * @param orderId
	 *            The order_id
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 
	 * @return The productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * 
	 * @param productId
	 *            The product_id
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The model
	 */
	public Object getModel() {
		return model;
	}

	/**
	 * 
	 * @param model
	 *            The model
	 */
	public void setModel(Object model) {
		this.model = model;
	}

	/**
	 * 
	 * @return The quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * 
	 * @param quantity
	 *            The quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * 
	 * @return The price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * 
	 * @param price
	 *            The price
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * 
	 * @return The total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 *            The total
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * 
	 * @return The tax
	 */
	public Object getTax() {
		return tax;
	}

	/**
	 * 
	 * @param tax
	 *            The tax
	 */
	public void setTax(Object tax) {
		this.tax = tax;
	}

	/**
	 * 
	 * @return The reward
	 */
	public Object getReward() {
		return reward;
	}

	/**
	 * 
	 * @param reward
	 *            The reward
	 */
	public void setReward(Object reward) {
		this.reward = reward;
	}

	/**
	 * 
	 * @return The status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The productModel
	 */
	public String getProductModel() {
		return productModel;
	}

	/**
	 * 
	 * @param productModel
	 *            The product_model
	 */
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	/**
	 * 
	 * @return The homeCounsultationCharges
	 */
	public String getHomeCounsultationCharges() {
		return homeCounsultationCharges;
	}

	/**
	 * 
	 * @param homeCounsultationCharges
	 *            The homeCounsultationCharges
	 */
	public void setHomeCounsultationCharges(String homeCounsultationCharges) {
		this.homeCounsultationCharges = homeCounsultationCharges;
	}

}