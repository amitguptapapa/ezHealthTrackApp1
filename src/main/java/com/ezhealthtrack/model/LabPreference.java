package com.ezhealthtrack.model;

import com.google.gson.annotations.Expose;

public class LabPreference {

	@Expose
	private String id;
	@Expose
	private String name;
	@Expose
	private String note;

	public boolean isChecked;

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
	 * @return The note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * 
	 * @param note
	 *            The note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}

}