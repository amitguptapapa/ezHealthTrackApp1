package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSets {

	@Expose
	private String id;
	@Expose
	private String type;
	@SerializedName("context_type")
	@Expose
	private String contextType;
	@SerializedName("context_id")
	@Expose
	private String contextId;
	@Expose
	private String tenant;
	@SerializedName("date_created")
	@Expose
	private String dateCreated;
	@SerializedName("date_updated")
	@Expose
	private String dateUpdated;
	@Expose
	private String status;
	@SerializedName("data_version")
	@Expose
	private Object dataVersion;
	@Expose
	private String name;
	@Expose
	private Object data;
	@SerializedName("created_by")
	@Expose
	private String createdBy;
	@SerializedName("updated_by")
	@Expose
	private String updatedBy;

	/**
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return The contextType
	 */
	public String getContextType() {
		return contextType;
	}

	/**
	 * 
	 * @param contextType
	 *            The context_type
	 */
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	/**
	 * 
	 * @return The contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * 
	 * @param contextId
	 *            The context_id
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * 
	 * @return The tenant
	 */
	public String getTenant() {
		return tenant;
	}

	/**
	 * 
	 * @param tenant
	 *            The tenant
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	/**
	 * 
	 * @return The dateCreated
	 */
	public String getDateCreated() {
		return dateCreated;
	}

	/**
	 * 
	 * @param dateCreated
	 *            The date_created
	 */
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * 
	 * @return The dateUpdated
	 */
	public String getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * 
	 * @param dateUpdated
	 *            The date_updated
	 */
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
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
	 * @return The dataVersion
	 */
	public Object getDataVersion() {
		return dataVersion;
	}

	/**
	 * 
	 * @param dataVersion
	 *            The data_version
	 */
	public void setDataVersion(Object dataVersion) {
		this.dataVersion = dataVersion;
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
	 * @return The data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 
	 * @return The createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 *            The created_by
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * 
	 * @return The updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * 
	 * @param updatedBy
	 *            The updated_by
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}