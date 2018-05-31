package com.ezhealthtrack.model.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOAPPhoto {

	@Expose
	private String id;
	@Expose
	private String rank;
	@Expose
	private String name;
	@Expose
	private String description;
	@SerializedName("date_created")
	@Expose
	private String dateCreated;
	@Expose
	private String preview;

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
	 * @return The rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * 
	 * @param rank
	 *            The rank
	 */
	public void setRank(String rank) {
		this.rank = rank;
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
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return The preview
	 */
	public String getPreview() {
		return preview;
	}

	/**
	 * 
	 * @param preview
	 *            The preview
	 */
	public void setPreview(String preview) {
		this.preview = preview;
	}

}